package com.chamika.newsapptest.presentation.dashboard.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chamika.newsapptest.data.models.ArticleX
import com.chamika.newsapptest.databinding.ListItemTopHeadlinesBinding

class NewsHeaderListItemAdapter(
    var context: Context,
    var clickListener: ((ArticleX) -> Unit)? = null,
) : RecyclerView.Adapter<NewsHeaderListItemAdapter.ItemViewHolder>() {

    private val itemList = ArrayList<ArticleX>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context.applicationContext
        val itemBinding = ListItemTopHeadlinesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(itemBinding)
    }

    fun setItemList(list: List<ArticleX>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem, context, clickListener, position, itemList)

    }

    override fun getItemCount() = itemList.size

    inner class ItemViewHolder(val binding: ListItemTopHeadlinesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currentItem: ArticleX,
            context: Context,
            clickListener: ((ArticleX) -> Unit)?,
            position: Int,
            itemList: ArrayList<ArticleX>
        ) {
            binding.container.setOnClickListener {
                clickListener?.invoke(currentItem)
            }

            currentItem.title?.let {
                binding.tvHeadline.text = it
            }

            currentItem.author?.let {
                binding.tvAuthorName.text = it
            }

            currentItem.description?.let {
                binding.tvNewsDescription.text = it
            }

            currentItem.urlToImage?.let {
                binding.imgHeadNews.load(
                    it
                ) {
                    crossfade(true)
                }
            }


        }
    }

}