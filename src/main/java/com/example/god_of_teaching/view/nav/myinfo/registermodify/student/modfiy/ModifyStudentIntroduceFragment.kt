package com.example.god_of_teaching.view.nav.myinfo.registermodify.student.modfiy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.databinding.FragmentTeacherIntroduceBinding
import com.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import com.example.god_of_teaching.viewmodel.StudentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class ModifyStudentIntroduceFragment  : Fragment() {
    private var existingItem  : String?=null
    private var _binding : FragmentTeacherIntroduceBinding?=null
    private val binding get() = _binding!!
    private val studentView : StudentViewModel by viewModels()
    //코루틴 정지 시키기 위해서 선언함
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentTeacherIntroduceBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        existingIntroduce()
        basicSetting()
        next()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel() // 코루틴 작업 취소
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        binding.btnCompleteIntroduceTeacher.text = "확인"
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarIntroduceTeacher,"자기소개 변경",binding.toolbarTitleIntroduceTeacher)
        NavigationUtil.handleBackPress(this,navController)
    }
    //기존 소개 불러오기
    private fun existingIntroduce()
    {
        //위에 선언해뒀다 Io로
        coroutineScope.launch {
            val studentDataStoreHelper = StudentDatastoreHelper.getInstance(requireActivity())
            studentDataStoreHelper.getIntroduce.collect { value ->
                existingItem = value
                withContext(Dispatchers.Main) { // UI 스레드로 전환
                    binding.etIntroduceTeacher.setText(existingItem)
                }
            }
        }
    }
    //변경 완료
    private fun next()
    {
        binding.btnCompleteIntroduceTeacher.setOnClickListener {
            val introduce = binding.etIntroduceTeacher.text.toString()
            studentView.uplodeStudentIntroduce(introduce)
            studentView.uplodeStudentIntroduceLocal(introduce)
            val navController = findNavController()
            navController.popBackStack()
        }
    }
}