package god.example.god_of_teaching.view.nav.find.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterAcademyListBinding
import god.example.god_of_teaching.model.dataclass.AcademyInfo


class AcademyPagingListAdapter(private val onClick: (AcademyInfo) -> Unit,
                               private val onClick2: (AcademyInfo) -> Unit,
                               private val onClick3: (AcademyInfo) -> Unit,
                               private val wishListCheck: List<AcademyInfo>?
) : PagingDataAdapter<AcademyInfo,AcademyPagingListAdapter.AcademyViewHolder>(AcademyComparator){
    inner class AcademyViewHolder(private val binding: AdapterAcademyListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(academy: AcademyInfo, onClick: (AcademyInfo) -> Unit) {
            val isWishlisted = wishListCheck?.any { it.uid == academy.uid } ?: false//위시리스트체크
            if(isWishlisted)
            {
                binding.ivWishlistAcademyList.setImageResource(R.drawable.redheart)
            }

                //위시리스트 버튼 처리
                binding.ivWishlistAcademyList.setOnClickListener {
                    val currentImage = binding.ivWishlistAcademyList.drawable.constantState?.newDrawable()
                    //위시리스트에 안 담겼을 때
                    if(currentImage?.constantState?.equals(binding.root.context.getDrawable(R.drawable.blackheart)?.constantState) == true)
                    {
                        binding.ivWishlistAcademyList.setImageResource(R.drawable.redheart)
                        onClick2(academy)
                    }
                    else
                    {
                        binding.ivWishlistAcademyList.setImageResource(R.drawable.blackheart)
                        onClick3(academy)
                    }

                }


            binding.tvNicknameAcademyList.text = academy.nickname
            binding.tvUserLocalAcademyList.text = academy.detailAddress
            binding.tvUserSubjectAcadmeyList.text = academy.subject.toString()
            binding.root.setOnClickListener {
                onClick(academy)
            }

        }
    }


    override fun onBindViewHolder(holder: AcademyViewHolder, position: Int) {
        val academyInfo = getItem(position)

        if (academyInfo != null) {
            holder.bind(academyInfo,onClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcademyViewHolder {

       val binding  = AdapterAcademyListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AcademyViewHolder(binding)
    }
    object AcademyComparator : DiffUtil.ItemCallback<AcademyInfo>() {
        override fun areItemsTheSame(oldItem: AcademyInfo, newItem: AcademyInfo): Boolean {
            // 아이템이 같은지 비교하는 로직
            return oldItem.nickname == newItem.nickname &&
                    oldItem.detailAddress == newItem.detailAddress &&
                    oldItem.subject == newItem.subject
        }

        override fun areContentsTheSame(oldItem: AcademyInfo, newItem: AcademyInfo): Boolean {
            // 아이템의 내용이 같은지 비교하는 로직
            return oldItem == newItem
        }
    }
}