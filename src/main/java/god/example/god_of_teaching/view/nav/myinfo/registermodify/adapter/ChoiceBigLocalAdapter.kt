package god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterBigLocalBinding

//광역시, 특별시, 도나타내는 어뎁터
class ChoiceBigLocalAdapter(private val dataSet: List<String>,private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<ChoiceBigLocalAdapter.ViewHolder>(){
    var selectedPosition = -1
    inner class ViewHolder(private val binding: AdapterBigLocalBinding, private val onItemClick: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String,position: Int)
        {
            binding.tvBigLocal.text = item
            if (position == selectedPosition) {
                binding.tvBigLocal.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.theme_color)) // 선택되면 배경색을 파란색으로
                binding.tvBigLocal.setTextColor(Color.WHITE) // 선택되면 텍스트 색상을 하얀색으로
            } else {
                binding.tvBigLocal.setBackgroundColor(Color.TRANSPARENT) // 아니면 투명
                binding.tvBigLocal.setTextColor(Color.BLACK) // 기본 텍스트 색상
            }
            binding.tvBigLocal.setOnClickListener {
                val previousSelectedPosition = selectedPosition  // 현재 선택된 위치를 임시 변수에 저장
                selectedPosition = position

                if (previousSelectedPosition != -1) {
                    notifyItemChanged(previousSelectedPosition)  // 이전 항목 업데이트
                }
                notifyItemChanged(selectedPosition)  // 새롭게 선택된 항목 업데이트
                onItemClick(position)

            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterBigLocalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding,onItemClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val bigLocal = dataSet[position]
            viewHolder.bind(bigLocal,position)

    }

    override fun getItemCount() =  dataSet.size
}