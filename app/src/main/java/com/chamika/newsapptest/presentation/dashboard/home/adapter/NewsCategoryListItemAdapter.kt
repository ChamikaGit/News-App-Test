package com.chamika.newsapptest.presentation.dashboard.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chamika.newsapptest.databinding.ListItemCategoriesBinding

class NewsCategoryListItemAdapter(
    private var context: Context,
    private var clickListener: ((String) -> Unit)? = null,
) : RecyclerView.Adapter<NewsCategoryListItemAdapter.ItemViewHolder>() {

    private val itemList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context.applicationContext
        val itemBinding = ListItemCategoriesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(itemBinding)
    }

    fun setItemList(list: List<String>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem, clickListener)

    }

    override fun getItemCount() = itemList.size

    inner class ItemViewHolder(val binding: ListItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currentItem: String,
            clickListener: ((String) -> Unit)?,
        ) {
            binding.container.setOnClickListener {
                clickListener?.invoke(currentItem)
            }

            binding.tvName.text = currentItem

        }
    }

}