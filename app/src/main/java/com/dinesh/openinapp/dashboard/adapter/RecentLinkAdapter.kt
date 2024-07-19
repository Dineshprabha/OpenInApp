package com.dinesh.openinapp.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinesh.openinapp.dashboard.model.RecentLink
import com.dinesh.openinapp.databinding.LayoutTopLinksItemBinding

class RecentLinkAdapter : RecyclerView.Adapter<RecentLinkAdapter.LinkViewHolder>() {

    inner class LinkViewHolder(private val binding: LayoutTopLinksItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recentLink: RecentLink) {
            binding.apply {
                Glide.with(itemView).load(recentLink.original_image).into(imgOriginal)
                tvAppName.text = recentLink.app
                tvDate.text = recentLink.created_at
                tvTotalClicks.text = recentLink.total_clicks.toString()
                tvWebLink.text = recentLink.web_link

            }
        }

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<RecentLink>() {
        override fun areItemsTheSame(oldItem: RecentLink, newItem: RecentLink): Boolean {
            return oldItem.url_id == newItem.url_id
        }

        override fun areContentsTheSame(oldItem: RecentLink, newItem: RecentLink): Boolean {
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
        val recentLink = differ.currentList[position]
        holder.bind(recentLink)
    }
}