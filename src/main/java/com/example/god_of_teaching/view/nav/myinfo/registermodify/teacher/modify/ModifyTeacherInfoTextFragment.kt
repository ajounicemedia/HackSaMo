package com.example.god_of_teaching.view.nav.myinfo.registermodify.teacher.modify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.databinding.FragmentModifyMyInfoTextBinding
import com.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import com.example.god_of_teaching.viewmodel.TeacherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//텍스트에 있는 선생님 정보 수정하기 위한 프래그먼트
@AndroidEntryPoint
class ModifyTeacherInfoTextFragment : Fragment() {

    private var _binding: FragmentModifyMyInfoTextBinding?=null
    private val binding get() = _binding!!
    private val teacherView: TeacherViewModel by viewModels()
    private var modifiedItem : String?=null
    private var existingItem  : String?=null
    //코루틴 정지 시키기 위해서 선언함
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         modifiedItem = arguments?.getString("modified_item")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentModifyMyInfoTextBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modifiedItem?.let { modifyTeacherInfo(it) }
        basicSetting()
        modifiedArticle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
       coroutineScope.cancel() // 코루틴 작업 취소
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixTextInfo,
            "$modifiedItem 수정",binding.toolbarTitleFixTextInfo)
        NavigationUtil.handleBackPress(this,navController)
    }
    //어떤 항목 수정할지 데이터 받기
    private fun modifiedArticle()
    {
        when(modifiedItem)
        {
            // 여기 있는 코루틴 위에 선언 해줬다
            "닉네임" ->   coroutineScope.launch {
                val teacherDataStoreHelper = TeacherDataStoreHelper.getInstance(requireActivity())
                teacherDataStoreHelper.getNickname.collect { value ->
                    existingItem = value
                    withContext(Dispatchers.Main) { // UI 스레드로 전환

                        binding.etChangeDataModifyMyInfoText.setText(existingItem)
                    }
                }
            }
            // 여기 있는 코루틴 위에 선언 해줬다
            "한줄소개"->   coroutineScope.launch {
                val teacherDataStoreHelper = TeacherDataStoreHelper.getInstance(requireActivity())
                teacherDataStoreHelper.getDes.collect { value ->
                    existingItem = value
                    withContext(Dispatchers.Main) { // UI 스레드로 전환
                        binding.etChangeDataModifyMyInfoText.setText(existingItem)
                    }
                }
            }
        }

    }
    //선택한 정보 변경
        private fun modifyTeacherInfo(modifyItem: String)
        {
            binding.btnCompleteModifyMyInfoText.setOnClickListener{
            val modifyName = binding.etChangeDataModifyMyInfoText.text.toString()

            teacherView.modifyTeacherInfo(modifyItem, modifyName)
                val navController = findNavController()
                navController.popBackStack()
           }
        }

}