package god.example.god_of_teaching.view.nav.mywishlist.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterTeacherListBinding
import god.example.god_of_teaching.model.dataclass.TeacherInfo

//내 선생 위시리스트 어댑터
class MyTeacherWishListAdapter(private val onClick: (TeacherInfo) -> Unit,
                               private val onClick2: (TeacherInfo) -> Unit) : ListAdapter<TeacherInfo,  MyTeacherWishListAdapter.TeacherViewHolder>(differ) {
    inner class TeacherViewHolder(private val binding: AdapterTeacherListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: TeacherInfo, onClick: (TeacherInfo) -> Unit) {
            //위시리스트이므로 첫 색깔 붉은 색
            binding.ivWishlistTeacherList.setImageResource(R.drawable.redheart)
            binding.ivWishlistTeacherList.setOnClickListener {
                binding.ivWishlistTeacherList.setImageResource(R.drawable.blackheart)
                onClick2(teacher)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTeacherWishListAdapter.TeacherViewHolder {
        // 뷰 홀더 생성
        val binding = AdapterTeacherListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyTeacherWishListAdapter.TeacherViewHolder, position: Int) {
        val teacherInfo = getItem(position)

        if (teacherInfo != null) {
            holder.bind(teacherInfo,onClick)
        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<TeacherInfo>() {
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
}