package com.chamika.newsapptest.presentation.details

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.chamika.newsapptest.databinding.FragmentNewsDetailsBinding
import com.chamika.newsapptest.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

@AndroidEntryPoint
class NewsDetailsFragment : BaseFragment() {

    private var _binding: FragmentNewsDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsDetailsViewModel by viewModels()
    private val args: NewsDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Blurry.with(context)
            .radius(10)
            .sampling(8)
            .color(Color.argb(245f, 245f, 245f, 0.5f))
            .async()
            .animate(500)
            .onto(binding.detailsContainer)

        binding.relBackContainer.setOnClickListener {
            findNavController().popBackStack()
        }

        args.newsArticle?.let { articleItems ->

            articleItems.urlToImage?.let {
                binding.imgHeadNews.load(
                    it
                ) {
                    crossfade(true)
                }
            }

            articleItems.publishedAt?.let {
                binding.tvDate.text = it
            }

            articleItems.description?.let {
                binding.tvTitle.text = it
            }

            articleItems.author?.let {
                binding.tvTitle.text = "Published by $it"
            }

            articleItems.content?.let {
                binding.tvNewsDescription.text = it
            }



        }


    }

}