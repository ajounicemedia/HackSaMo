package god.example.god_of_teaching.view.nav.find.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterTeacherListBinding
import god.example.god_of_teaching.model.dataclass.TeacherInfo

class TeacherPagingListAdapter(private val onClick: (TeacherInfo) -> Unit,
                               private val onclick2: (TeacherInfo) -> Unit,
                               private val onClick3: (TeacherInfo) -> Unit,
                               private val wishListCheck: List<TeacherInfo>?
                                ) : PagingDataAdapter<TeacherInfo, TeacherPagingListAdapter.TeacherViewHolder>(TeacherComparator) {

    inner class TeacherViewHolder(private val binding: AdapterTeacherListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: TeacherInfo, onClick: (TeacherInfo) -> Unit) {
            val isWishlisted = wishListCheck?.any { it.uid == teacher.uid } ?: false//위시리스트체크

            if(isWishlisted)
            {
                binding.ivWishlistTeacherList.setImageResource(R.drawable.redheart)

            }

            binding.ivWishlistTeacherList.setOnClickListener {
                val currentImage = binding.ivWishlistTeacherList.drawable.constantState?.newDrawable()
                //위시리스트에 안 담겼을 때
                if(currentImage?.constantState?.equals(binding.root.context.getDrawable(R.drawable.blackheart)?.constantState) == true)
                {
                    binding.ivWishlistTeacherList.setImageResource(R.drawable.redheart)
                    onclick2(teacher)
                }
                else
                {
                    binding.ivWishlistTeacherList.setImageResource(R.drawable.blackheart)
                    onClick3(teacher)
                }
            }
            binding.tvNicknameTeacherList.text = teacher.nickname
            binding.tvUserAbilityTeacherList.text = teacher.campus
            binding.tvUserLocalTeacherList.text = teacher.availableLocal.toString()
            binding.tvUserSubjectTeacherList.text = teacher.subject.toString()
            binding.root.setOnClickListener {
                onClick(teacher)
            }
        }
    }


    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacherInfo = getItem(position)

        if (teacherInfo != null) {
            holder.bind(teacherInfo,onClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        // 뷰 홀더 생성
        val binding = AdapterTeacherListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TeacherViewHolder(binding)
    }


    object TeacherComparator : DiffUtil.ItemCallback<TeacherInfo>() {
        override fun areItemsTheSame(oldItem: TeacherInfo, newItem: TeacherInfo): Boolean {
            // 아이템이 같은지 비교하는 로직
            return oldItem.nickname == newItem.nickname &&
                    oldItem.availableLocal == newItem.availableLocal &&
                    oldItem.subject == newItem.subject
        }

        override fun areContentsTheSame(oldItem: TeacherInfo, newItem: TeacherInfo): Boolean {
            // 아이템의 내용이 같은지 비교하는 로직
            return oldItem == newItem
        }
    }
}