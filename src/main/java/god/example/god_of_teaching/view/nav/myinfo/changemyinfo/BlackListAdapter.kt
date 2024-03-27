package god.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.databinding.AdapterBlacklistBinding


class BlackListAdapter(private val onItemClick: (String) -> Unit)
    : ListAdapter<String,BlackListAdapter.ViewHolder>(differ) {

    inner class ViewHolder(private val binding: AdapterBlacklistBinding,
                           private val onItemClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String)
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
        holder.bind(currentList[position])
    }

    companion object{
        val differ = object: DiffUtil.ItemCallback<String>()
        {
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem

            }

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }



}