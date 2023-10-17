package com.chamika.newsapptest.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chamika.newsapptest.BuildConfig
import com.chamika.newsapptest.R
import com.chamika.newsapptest.data.util.Resource
import com.chamika.newsapptest.databinding.FragmentSearchBinding
import com.chamika.newsapptest.presentation.BaseFragment
import com.chamika.newsapptest.presentation.adapter.NewsCategoryListItemAdapter
import com.chamika.newsapptest.presentation.adapter.NewsSearchListItemAdapter
import com.chamika.newsapptest.presentation.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private val TAG = "SearchFragment"
    private lateinit var newsSearchAdapter: NewsSearchListItemAdapter
    private lateinit var newsCategoryListItemAdapter: NewsCategoryListItemAdapter
    private val args: SearchFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData.observe(this) {
            viewModel.isNetworkAvailable.value = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        args.searchText?.let {
            binding.etSearch.setText(it)
            viewModel.getNewsSearchData(searchQuery = it, sortBy = viewModel.sortBy)
        }
        iniRecyclerView()
        bindResponseData()
        searchNavigation()
    }

    private fun searchNavigation() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.getNewsSearchData(
                    sortBy = viewModel.sortBy,
                    searchQuery = binding.etSearch.text.toString().trim()
                )
            }
            true
        }
    }

    private fun bindResponseData() {

        viewModel.newsSearchLiveData.observe(viewLifecycleOwner) { categoryDataResponse ->
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
                                binding.tvSearchCount.text =
                                    "About ${it.size} results for ${binding.etSearch.text}"

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

        newsSearchAdapter = NewsSearchListItemAdapter(context = requireActivity(), clickListener = {
            val bundle = Bundle().apply {
                putSerializable(Constant.article, it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_newsDetailsFragment, bundle)
        })

        newsCategoryListItemAdapter =
            NewsCategoryListItemAdapter(context = requireActivity(), clickListener = {
                viewModel.sortBy = it
                viewModel.getNewsSearchData(
                    sortBy = viewModel.sortBy,
                    searchQuery = binding.etSearch.toString().trim()
                )

            })

        newsCategoryListItemAdapter.setItemList(viewModel.getCategoriesList())

        binding.rvLatestNews.apply {
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