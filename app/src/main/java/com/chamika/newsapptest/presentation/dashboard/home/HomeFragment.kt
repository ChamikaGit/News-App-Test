package com.chamika.newsapptest.presentation.dashboard.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chamika.newsapptest.BuildConfig
import com.chamika.newsapptest.data.util.Resource
import com.chamika.newsapptest.databinding.FragmentHomeBinding
import com.chamika.newsapptest.presentation.BaseFragment
import com.chamika.newsapptest.presentation.dashboard.home.adapter.NewsCategoryListItemAdapter
import com.chamika.newsapptest.presentation.dashboard.home.adapter.NewsHeaderListItemAdapter
import com.chamika.newsapptest.presentation.dashboard.home.adapter.NewsSearchListItemAdapter
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun bindResponseData() {
        viewModel.newsHeadlineLiveData.observe(viewLifecycleOwner) { topHeadlineResponse ->
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
                                newsAdapter.setItemList(list = it)
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

        viewModel.newsCategoryLiveData.observe(viewLifecycleOwner) { categoryDataResponse ->
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


    private fun iniRecyclerView() {
        newsAdapter = NewsHeaderListItemAdapter(context = requireActivity(), clickListener = {

        })

        newsSearchAdapter = NewsSearchListItemAdapter(context = requireActivity(), clickListener = {

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
}