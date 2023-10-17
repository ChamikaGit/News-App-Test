package com.chamika.newsapptest.presentation.hotnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chamika.newsapptest.R
import com.chamika.newsapptest.databinding.FragmentHotNewsBinding
import com.chamika.newsapptest.presentation.BaseFragment
import com.chamika.newsapptest.presentation.adapter.NewsTopListItemAdapter
import com.chamika.newsapptest.presentation.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotNewsFragment : BaseFragment() {

    private var _binding: FragmentHotNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HotNewsViewModel by viewModels()
    private val args: HotNewsFragmentArgs by navArgs()
    private lateinit var newsTopListItemAdapter: NewsTopListItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        iniRecyclerView()

    }

    private fun iniRecyclerView() {

        newsTopListItemAdapter =
            NewsTopListItemAdapter(context = requireActivity(), clickListener = {
                val bundle = Bundle().apply {
                    putSerializable(Constant.article, it)
                }
                findNavController().navigate(
                    R.id.action_hotNewsFragment_to_newsDetailsFragment,
                    bundle
                )
            })

        val hotNews = args.newsArticleList

        hotNews?.let { hotNews ->
            viewModel.setHotNewsList(hotNews.articles)

            viewModel.hotNewsListLiveData.value?.let {
                newsTopListItemAdapter.setItemList(it)
            }

            binding.rvLatestNews.apply {
                adapter = newsTopListItemAdapter
                layoutManager = LinearLayoutManager(
                    requireActivity(), LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }

    }


}