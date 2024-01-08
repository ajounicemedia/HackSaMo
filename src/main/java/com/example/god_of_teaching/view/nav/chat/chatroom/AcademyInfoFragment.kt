package com.example.god_of_teaching.view.nav.chat.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.god_of_teaching.databinding.FragmentAcademyInfoBinding
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.viewmodel.AcademyChatViewModel
import dagger.hilt.android.AndroidEntryPoint

//채팅창에서 학원 보기
@AndroidEntryPoint
class AcademyInfoFragment: Fragment() {
    private var _binding: FragmentAcademyInfoBinding?=null
    private val academyChatView : AcademyChatViewModel by viewModels()
    private val binding get() = _binding!!
    private var otherUid : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherUid = arguments?.getString("other_uid")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAcademyInfoBinding.inflate(inflater,container,false)



        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        uiSetting()
        loadProfileImage()
        otherUid?.let { academyChatView.getAcademyInfo(it) }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixAcademyInfo,"선택된 선생님 정보",binding.toolbarTitleFixAcademyInfo)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //프사 불러오기
    private fun loadProfileImage()
    {
        val imageUrl = arguments?.getString("academyImageUrl") ?: return
        Glide.with(this)
            .load(imageUrl)
            .transform(CircleCrop()) // CircleCrop 변환 적용
            .into(binding.ivProfileImgAcademyInfo)
    }
    //선생님 정보 ui에 할당
    private fun uiSetting()
    {
        academyChatView.academyInfo?.observe(viewLifecycleOwner) { academyInfo ->
            binding.tvMyNameAcademyInfo.text = academyInfo.name
            binding.tvMyDesAcademyInfo.text = academyInfo.des
            binding.tvMyClassAgeAcademyInfo.text = academyInfo.des
            binding.tvMyIntroduceAcademyInfo.text = academyInfo.introduce
            binding.tvMyLocalAcademyInfo.text = academyInfo.local
            binding.tvMyDetailAddressFixAcademyInfo.text =academyInfo.detailAddress
            binding.tvMySubjectFixAcademyInfo.text = (academyInfo.subject).toString()
        }
    }
}