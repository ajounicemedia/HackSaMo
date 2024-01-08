package com.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.god_of_teaching.databinding.AdapterBlacklistBinding
import com.example.god_of_teaching.databinding.AdapterLocalBinding


class BlackListAdapter(private val blackList: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<BlackListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: AdapterBlacklistBinding, private val onItemClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String,position: Int)
        {
            binding.tvBlacklist.text =item
            binding.btnRemoveBlacklist.setOnClickListener {
                onItemClick(item)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlackListAdapter.ViewHolder {
        val binding = AdapterBlacklistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding,onItemClick)
    }

    override fun onBindViewHolder(holder: BlackListAdapter.ViewHolder, position: Int) {
        val blackList = blackList[position]
        holder.bind(blackList,position)
    }

    override fun getItemCount() = blackList.size



}