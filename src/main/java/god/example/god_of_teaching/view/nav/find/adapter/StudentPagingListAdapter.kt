package god.example.god_of_teaching.view.nav.find.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterStudentListBinding
import god.example.god_of_teaching.model.dataclass.StudentInfo

//학생 페이징 어뎁터
class StudentPagingListAdapter(private val onClick: (StudentInfo)-> Unit,
                               private val onClick2: (StudentInfo)-> Unit,
                               private val onClick3: (StudentInfo)-> Unit,
                               private val wishListCheck: List<StudentInfo>?) :
    PagingDataAdapter<StudentInfo, StudentPagingListAdapter.StudentViewHolder>(StudentComparator) {

    inner class StudentViewHolder(private val binding: AdapterStudentListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student : StudentInfo, onClick: (StudentInfo) -> Unit) {
            val isWishlisted = wishListCheck?.any { it.uid == student.uid } ?: false//위시리스트체크

            if(isWishlisted)
            {
                binding.ivWishlistStudentList.setImageResource(R.drawable.redheart)

            }
            binding.ivWishlistStudentList.setOnClickListener {
                val currentImage = binding.ivWishlistStudentList.drawable.constantState?.newDrawable()
                //위시리스트에 안 담겼을 때
                if(currentImage?.constantState?.equals(binding.root.context.getDrawable(R.drawable.blackheart)?.constantState) == true)
                {
                    binding.ivWishlistStudentList.setImageResource(R.drawable.redheart)
                    onClick2(student)
                }
                else
                {
                    binding.ivWishlistStudentList.setImageResource(R.drawable.blackheart)
                    onClick3(student)
                }
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        // 뷰 홀더 생성
        val binding = AdapterStudentListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val studentInfo = getItem(position)

        if (studentInfo != null) {
            holder.bind(studentInfo,onClick)
        }
    }
    object StudentComparator : DiffUtil.ItemCallback<StudentInfo>() {
        override fun areItemsTheSame(oldItem: StudentInfo, newItem: StudentInfo): Boolean {
            // 아이템이 같은지 비교하는 로직
            return oldItem.nickname == newItem.nickname &&
                    oldItem.availableLocal == newItem.availableLocal &&
                    oldItem.subject == newItem.subject
        }

        override fun areContentsTheSame(oldItem: StudentInfo, newItem: StudentInfo): Boolean {
            // 아이템의 내용이 같은지 비교하는 로직
            return oldItem == newItem
        }
    }
}