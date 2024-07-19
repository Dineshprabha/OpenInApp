package com.dinesh.openinapp.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinesh.openinapp.dashboard.model.TopLink
import com.dinesh.openinapp.databinding.LayoutTopLinksItemBinding

class TopLinkAdapter : RecyclerView.Adapter<TopLinkAdapter.LinkViewHolder>() {

    inner class LinkViewHolder(private val binding: LayoutTopLinksItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(topLink: TopLink) {
            binding.apply {
                Glide.with(itemView).load(topLink.original_image).into(imgOriginal)
                tvAppName.text = topLink.app
                tvDate.text = topLink.created_at
                tvTotalClicks.text = topLink.total_clicks.toString()
                tvWebLink.text = topLink.web_link

            }
        }

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<TopLink>() {
        override fun areItemsTheSame(oldItem: TopLink, newItem: TopLink): Boolean {
            return oldItem.url_id == newItem.url_id
        }

        override fun areContentsTheSame(oldItem: TopLink, newItem: TopLink): Boolean {
            return oldItem.url_id == newItem.url_id
        }


    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        return LinkViewHolder(
            LayoutTopLinksItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val topLink = differ.currentList[position]
        holder.bind(topLink)
    }
}