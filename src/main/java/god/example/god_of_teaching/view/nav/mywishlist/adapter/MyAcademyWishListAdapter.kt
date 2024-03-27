package god.example.god_of_teaching.view.nav.mywishlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterAcademyListBinding
import god.example.god_of_teaching.model.dataclass.AcademyInfo

//내 학원 위시리스트 어댑터
class MyAcademyWishListAdapter(private val onClick: (AcademyInfo) -> Unit,
                               private val onClick2: (AcademyInfo) -> Unit) : ListAdapter<AcademyInfo, MyAcademyWishListAdapter.AcademyViewHolder>(differ){


    inner class AcademyViewHolder(private val binding: AdapterAcademyListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(academy: AcademyInfo, onClick: (AcademyInfo) -> Unit) {
            //위시리스트이므로 첫 색깔 붉은 색
            binding.ivWishlistAcademyList.setImageResource(R.drawable.redheart)
            binding.ivWishlistAcademyList.setOnClickListener {
                    binding.ivWishlistAcademyList.setImageResource(R.drawable.blackheart)
                    onClick2(academy)
            }
            binding.tvNicknameAcademyList.text = academy.nickname
            binding.tvUserLocalAcademyList.text = academy.detailAddress
            binding.tvUserSubjectAcadmeyList.text = academy.subject.toString()
            binding.root.setOnClickListener {
                onClick(academy)
            }

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAcademyWishListAdapter.AcademyViewHolder {

        val binding  = AdapterAcademyListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AcademyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AcademyViewHolder, position: Int) {
        val academyInfo = getItem(position)

        if (academyInfo != null) {
            holder.bind(academyInfo,onClick)
        }
    }

    companion object{
        val differ = object: DiffUtil.ItemCallback<AcademyInfo>()
        {
            override fun areContentsTheSame(oldItem: AcademyInfo, newItem: AcademyInfo): Boolean {
                return oldItem.nickname == newItem.nickname &&
                        oldItem.detailAddress == newItem.detailAddress &&
                        oldItem.subject == newItem.subject
            }

            override fun areItemsTheSame(oldItem: AcademyInfo, newItem: AcademyInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}