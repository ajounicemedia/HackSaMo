package com.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentManageUserStatusBinding
import com.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import com.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import com.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.viewmodel.MyInfoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//유저 상태 변경을 위한 프래그먼트
@AndroidEntryPoint
class ManageUserStatusFragment: Fragment() {
    private var _binding: FragmentManageUserStatusBinding?=null
    private val binding get() = _binding!!
    private val myInfoView: MyInfoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentManageUserStatusBinding.inflate(inflater,container,false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        setStatus()
        basicStatus()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        val navController = findNavController()
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarManageStatus,"내 상태 설정하기",binding.toolbarTitleManageStatus)
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //상태 설정
    private fun setStatus()
    {
        //선생님 활성
        binding.switchTeacher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                handleTeacherSwitch()
            }
        }
        //학원
        binding.switchAcademy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                handleAcademySwitch()
            }
        }
        //학생
        binding.switchStudent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                handleStudentSwitch()
            }
        }
    }
    //선생님 활성화 했을 때
    private fun handleTeacherSwitch()
    {
            val teacherDataStoreHelper = TeacherDataStoreHelper.getInstance(requireActivity())
            viewLifecycleOwner.lifecycleScope.launch{
                val value = teacherDataStoreHelper.getIntroduce.first()
                if(value!="default")
                {
                    binding.switchAcademy.isChecked = false
                    binding.switchStudent.isChecked = false
                    myInfoView.changeMyStatus("teacher")
                }
                else
                {
                    binding.switchTeacher.isChecked = false
                    Snackbar.make(binding.root, "선생님 등록 후 활성 가능합니다", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
    //학원 활성화 했을 때
    private fun handleAcademySwitch() {
        val academyDataStoreHelper = AcademyDatastoreHelper.getInstance(requireActivity())
        viewLifecycleOwner.lifecycleScope.launch {
            val value = academyDataStoreHelper.getIntroduce.first()
            if (value != "default")
            {
                binding.switchTeacher.isChecked = false
                binding.switchStudent.isChecked = false
                myInfoView.changeMyStatus("academy")
            }
            else
            {
                binding.switchAcademy.isChecked = false
                Snackbar.make(binding.root, "학원 등록 후 활성 가능합니다", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    //학생 활성화 했을 때
    private fun handleStudentSwitch()
    {
        if(binding.switchStudent.isChecked)
        {
            val studentDataStoreHelper = StudentDatastoreHelper.getInstance(requireActivity())
            viewLifecycleOwner.lifecycleScope.launch {
                val value = studentDataStoreHelper.getIntroduce.first()
                if (value!="default")
                {
                    binding.switchTeacher.isChecked = false
                    binding.switchAcademy.isChecked = false
                    myInfoView.changeMyStatus("student")
                }
                else
                {
                    binding.switchStudent.isChecked = false
                    Snackbar.make(binding.root, "학생 등록 후 활성 가능합니다", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
    //기본 상태 설정
    private fun basicStatus()
    {
        val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
        viewLifecycleOwner.lifecycleScope.launch {
            when(userDataStoreHelper.getStatus.first())
            {
                "teacher" ->  binding.switchTeacher.isChecked = true
                "student" -> binding.switchStudent.isChecked=true
                "academy" ->binding.switchAcademy.isChecked=true
            }
        }
    }


}