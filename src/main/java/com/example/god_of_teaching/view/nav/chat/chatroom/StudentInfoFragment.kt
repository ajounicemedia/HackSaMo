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
import com.example.god_of_teaching.databinding.FragmentStudentInfoBinding
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.viewmodel.StudentChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentInfoFragment: Fragment() {
    private var _binding: FragmentStudentInfoBinding?=null
    private val studentChatView : StudentChatViewModel by viewModels()
    private val binding get() = _binding!!
    private var otherUid : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //키 값 받아오기
        otherUid = arguments?.getString("other_uid")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentInfoBinding.inflate(inflater,container,false)



        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        uiSetting()
        loadProfileImage()
        otherUid?.let { studentChatView.getStudentInfo(it) }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixStudentInfo,"선택된 학생 정보",binding.toolbarTitleFixStudentInfo)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //학생 정보 ui에 할당
    private fun uiSetting()
    {
        studentChatView.studentInfo?.observe(viewLifecycleOwner) { studentInfo ->
            binding.tvMyNicknameFixStudentInfo.text = studentInfo.nickname
            binding.tvMyDesFixStudentInfo.text = studentInfo.des
            binding.tvMyBornYearFixStudentInfo.text = studentInfo.bornYear
            binding.tvMyGenderFixStudentInfo.text = studentInfo.gender
            binding.tvMyWayFixStudentInfo.text = studentInfo.way
            binding.tvMyLocalFixStudentInfo.text =studentInfo.availableLocal.toString()
            binding.tvMySubjectFixStudentInfo.text = (studentInfo.subject).toString()
            binding.tvMyIntroduceFixStudentInfo.text =studentInfo.introduce
        }
    }
    //프사 불러오기
    private fun loadProfileImage()
    {
        val imageUrl = arguments?.getString("studentImageUrl") ?: return
        Glide.with(this)
            .load(imageUrl)
            .transform(CircleCrop()) // CircleCrop 변환 적용
            .into(binding.ivProfileImgFixStudentInfo)
    }
}