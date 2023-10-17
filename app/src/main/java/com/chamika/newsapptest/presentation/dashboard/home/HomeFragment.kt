package com.chamika.newsapptest.presentation.dashboard.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chamika.newsapptest.BuildConfig
import com.chamika.newsapptest.R
import com.chamika.newsapptest.data.models.HotNews
import com.chamika.newsapptest.data.util.Resource
import com.chamika.newsapptest.databinding.FragmentHomeBinding
import com.chamika.newsapptest.presentation.BaseFragment
import com.chamika.newsapptest.presentation.adapter.NewsCategoryListItemAdapter
import com.chamika.newsapptest.presentation.adapter.NewsHeaderListItemAdapter
import com.chamika.newsapptest.presentation.adapter.NewsSearchListItemAdapter
import com.chamika.newsapptest.presentation.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val TAG = "HomeFragment"
    private lateinit var newsAdapter: NewsHeaderListItemAdapter
    private lateinit var newsSearchAdapter: NewsSearchListItemAdapter
    private lateinit var newsCategoryListItemAdapter: NewsCategoryListItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData.observe(this) {
            viewModel.isNetworkAvailable.value = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNewsHeadLines(country = "us")
        viewModel.getNewsCategories(country = "us", category = viewModel.categoryType)
        iniRecyclerView()
        bindResponseData()
        searchNavigation()

        binding.seeAllContainer.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToHotNewsFragment(viewModel.hotNewsListLiveData.value?.let { articleList ->
                    HotNews(articleList)
                })
            findNavController().navigate(action)
        }
    }

    private fun searchNavigation() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = binding.etSearch.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    val bundle = Bundle().apply {
                        putString(Constant.searchText, searchText)
                    }
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment, bundle)
                }
            }
            true
        }
    }

    private fun bindResponseData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newsHeadlineSharedFlow.collect { topHeadlineResponse ->
                topHeadlineResponse.data?.status?.let { responseStatus ->
                    if (responseStatus == "ok") {
                        when (topHeadlineResponse) {
                            is Resource.Loading -> {
                                binding.allNewsProgressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                binding.allNewsProgressBar.visibility = View.GONE
                                topHeadlineResponse.data.articles?.let {
                                    if (BuildConfig.DEBUG)
                                        Log.e(TAG, "onViewCreated: ${it.size}")
                                    if (it.size > 3) {
                                        newsAdapter.setItemList(list = it.subList(0, 3))
                                        viewModel.setHotNewsList(it)
                                    }
                                }
                            }
                            is Resource.Error -> {
                                binding.allNewsProgressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    topHeadlineResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            topHeadlineResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newsCategorySharedFlow.collect { categoryDataResponse ->
                categoryDataResponse.data?.status?.let { responseStatus ->
                    if (responseStatus == "ok") {
                        when (categoryDataResponse) {
                            is Resource.Loading -> {
                                binding.allNewsProgressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                binding.allNewsProgressBar.visibility = View.GONE
                                categoryDataResponse.data.articles?.let {
                                    if (BuildConfig.DEBUG)
                                        Log.e(TAG, "onViewCreated: ${it.size}")
                                    newsSearchAdapter.setItemList(list = it)
                                }
                            }
                            is Resource.Error -> {
                                binding.allNewsProgressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    categoryDataResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            categoryDataResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


            }
        }
    }


    private fun iniRecyclerView() {
        newsAdapter = NewsHeaderListItemAdapter(context = requireActivity(), clickListener = {

            val bundle = Bundle().apply {
                putSerializable(Constant.article, it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_newsDetailsFragment, bundle)

        })

        newsSearchAdapter = NewsSearchListItemAdapter(context = requireActivity(), clickListener = {

            val bundle = Bundle().apply {
                putSerializable(Constant.article, it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_newsDetailsFragment, bundle)
        })

        newsCategoryListItemAdapter =
            NewsCategoryListItemAdapter(context = requireActivity(), clickListener = {
                viewModel.categoryType = it
                viewModel.getNewsCategories(country = "us", category = viewModel.categoryType)

            })

        newsCategoryListItemAdapter.setItemList(viewModel.getCategoriesList())

        binding.rvLatestNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        binding.rvCategoriesNews.apply {
            adapter = newsSearchAdapter
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL,
                false
            )
        }

        binding.rvCategories.apply {
            adapter = newsCategoryListItemAdapter
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etSearch.setText("")
    }
}