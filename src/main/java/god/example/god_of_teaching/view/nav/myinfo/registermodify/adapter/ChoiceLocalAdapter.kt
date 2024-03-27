package god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterLocalBinding

//구,읍,면 등을 선택하는 어댑터
class ChoiceLocalAdapter(private val dataSet: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<ChoiceLocalAdapter.ViewHolder>(){
    var selectedPosition = -1
    inner class ViewHolder(private val binding: AdapterLocalBinding, private val onItemClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String,position: Int)
        {
            binding.tvLocal.text = "         "+item
            if (position == selectedPosition) {

                binding.tvLocal.setTextColor(ContextCompat.getColor(binding.root.context, R.color.theme_color)) // 선택되면 텍스트 색상을 하얀색으로
            } else {
                binding.tvLocal.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_theme_color)) // 기본 텍스트 색상
            }
            binding.tvLocal.setOnClickListener {
                val previousSelectedPosition = selectedPosition  // 현재 선택된 위치를 임시 변수에 저장
                selectedPosition = position
                if (previousSelectedPosition != -1) {
                    notifyItemChanged(previousSelectedPosition)  // 이전 항목 업데이트
                }
                notifyItemChanged(selectedPosition)  // 새롭게 선택된 항목 업데이트
                onItemClick(item)
            }
        }


    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = AdapterLocalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding,onItemClick)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val local = dataSet[position]
        viewHolder.bind(local,position)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() =  dataSet.size
}