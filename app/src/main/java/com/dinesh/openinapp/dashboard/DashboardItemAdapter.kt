package com.dinesh.openinapp.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinesh.openinapp.dashboard.model.DashboardItem
import com.dinesh.openinapp.databinding.DashboardLayoutItemBinding


class DashboardItemAdapter : RecyclerView.Adapter<DashboardItemAdapter.DashboardItemViewHolder>() {

    inner class DashboardItemViewHolder(private val binding: DashboardLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dashboardItem: DashboardItem) {
            binding.apply {
                Glide.with(itemView).load(dashboardItem.image).into(dashBoardIcon)
                tvName.text = dashboardItem.name
                tvNameValue.text = dashboardItem.nameValue

            }
        }

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<DashboardItem>() {
        override fun areItemsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean {
            return oldItem.name == newItem.name
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardItemViewHolder {
        return DashboardItemViewHolder(
            DashboardLayoutItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DashboardItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }
}