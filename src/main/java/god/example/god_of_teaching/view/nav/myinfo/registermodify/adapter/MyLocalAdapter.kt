package god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.databinding.AdapterMyLocalBinding

//내가 선택한 지역을 보여주는 어뎁터
class MyLocalAdapter(private val dataSet: MutableList<String>, private val onItemClicked: (Int) -> Unit) : RecyclerView.Adapter<MyLocalAdapter.ViewHolder>(){
    class ViewHolder(private val binding: AdapterMyLocalBinding, private val onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(local: String, position: Int)
        {
                binding.tvMyLocal.text = local
                binding.ivDeleteLocal.setOnClickListener {
                    onItemClicked(position)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMyLocalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding, onItemClicked)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val local = dataSet[position]
        viewHolder.bind(local,position)


    }


}