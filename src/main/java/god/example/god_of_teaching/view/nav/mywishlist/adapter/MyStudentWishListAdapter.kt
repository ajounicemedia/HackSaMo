package god.example.god_of_teaching.view.nav.mywishlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterStudentListBinding
import god.example.god_of_teaching.model.dataclass.StudentInfo


class MyStudentWishListAdapter(private val onClick: (StudentInfo) -> Unit,
                               private val onClick2: (StudentInfo) -> Unit) : ListAdapter<StudentInfo, MyStudentWishListAdapter.StudentViewHolder>(differ) {

    inner class StudentViewHolder(private val binding: AdapterStudentListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student : StudentInfo, onClick: (StudentInfo) -> Unit) {
            binding.ivWishlistStudentList.setImageResource(R.drawable.redheart)
            binding.ivWishlistStudentList.setOnClickListener {
                    binding.ivWishlistStudentList.setImageResource(R.drawable.blackheart)
                    onClick2(student)
            }
            binding.tvNicknameStudentList.text = student.nickname
            binding.tvUserBornYearStudentList.text = "  "+student.bornYear
            binding.tvUserLocalStudentList.text = "  "+student.availableLocal.toString()
            binding.tvUserSubjectStudentList.text = "  "+student.subject.toString()
            binding.root.setOnClickListener {
                onClick(student)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyStudentWishListAdapter.StudentViewHolder {

        val binding  = AdapterStudentListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val studentInfo = getItem(position)

        if (studentInfo != null) {
            holder.bind(studentInfo,onClick)
        }
    }
    companion object{
        val differ = object: DiffUtil.ItemCallback<StudentInfo>()
        {
            override fun areContentsTheSame(oldItem: StudentInfo, newItem: StudentInfo): Boolean {
                return oldItem.nickname == newItem.nickname &&
                        oldItem.availableLocal == newItem.availableLocal &&
                        oldItem.subject == newItem.subject
            }

            override fun areItemsTheSame(oldItem: StudentInfo, newItem: StudentInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}