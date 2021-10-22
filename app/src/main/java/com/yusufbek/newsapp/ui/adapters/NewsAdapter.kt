package com.yusufbek.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusufbek.newsapp.databinding.ItemArticlePreviewBinding
import com.yusufbek.newsapp.ui.retrofit.ArticleEntity

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private lateinit var binding: ItemArticlePreviewBinding

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ArticleEntity>() {
        override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(list: List<ArticleEntity>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        binding =
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class NewsViewHolder(binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articleEntity: ArticleEntity) {
            Glide.with(binding.root.context).load(articleEntity.urlToImage).into(binding.ivArticleImage)
            binding.tvSource.text = articleEntity.source?.name
            binding.tvTitle.text = articleEntity.title
            binding.tvDescription.text = articleEntity.description
            binding.tvPublishedAt.text = articleEntity.publishedAt
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(articleEntity)
                }
            }
        }

    }

    private var onItemClickListener: ((ArticleEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (ArticleEntity) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}