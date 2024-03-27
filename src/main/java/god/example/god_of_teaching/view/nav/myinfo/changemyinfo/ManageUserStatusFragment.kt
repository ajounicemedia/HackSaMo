package god.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentManageUserStatusBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.MyInfoViewModel


//유저 상태 변경을 위한 프래그먼트
@AndroidEntryPoint
class ManageUserStatusFragment: Fragment() {
    private var _binding: FragmentManageUserStatusBinding?=null
    private val binding get() = _binding!!
    private val myInfoView: MyInfoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
                //handleTeacherSwitch()
                myInfoView.changeMyStatus("teacher")
            }
        }
        //학원
        binding.switchAcademy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                //handleAcademySwitch()
                myInfoView.changeMyStatus("academy")
            }
        }
        //학생
        binding.switchStudent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                //handleStudentSwitch()
                myInfoView.changeMyStatus("student")
            }
        }
    }
    //선생님 활성화 했을 때
    private fun handleTeacherSwitch()
    {
        myInfoView.teacherCheck.observe(viewLifecycleOwner) { teacherCheck->
            if (teacherCheck=="true")
                {
                    myInfoView.changeMyStatus("teacher")
                    binding.switchAcademy.isChecked = false
                    binding.switchStudent.isChecked = false
                    //binding.switchTeacher.isChecked = true

                }
                else
                {
                    //binding.switchTeacher.isChecked = false
                    Snackbar.make(binding.root, "선생님 등록 후 활성 가능합니다", Snackbar.LENGTH_SHORT).show()
                }
            }
        myInfoView.teacherCheck.removeObserver {  }
    }
    //학원 활성화 했을 때
    private fun handleAcademySwitch() {
        myInfoView.academyCheck.observe(viewLifecycleOwner){academyCheck->
            if (academyCheck=="true")
            {
                myInfoView.changeMyStatus("academy")
                binding.switchTeacher.isChecked = false
                binding.switchStudent.isChecked = false
                //binding.switchAcademy.isChecked = true
            }
            else
            {
                //binding.switchAcademy.isChecked = false
                Snackbar.make(binding.root, "학원 등록 후 활성 가능합니다", Snackbar.LENGTH_SHORT).show()
            }
        }
        myInfoView.academyCheck.removeObserver {  }
    }
    //학생 활성화 했을 때
    private fun handleStudentSwitch()
    {
        if(binding.switchStudent.isChecked)
        {
            myInfoView.studentCheck.observe(viewLifecycleOwner){studentCheck->
                if (studentCheck=="true")
                {
                    myInfoView.changeMyStatus("student")
                    binding.switchTeacher.isChecked = false
                    binding.switchAcademy.isChecked = false
                    //binding.switchStudent.isChecked = true

                }
                else
                {
                    //binding.switchStudent.isChecked = false
                    Snackbar.make(binding.root, "학생 등록 후 활성 가능합니다", Snackbar.LENGTH_SHORT).show()
                }
            }
            myInfoView.studentCheck.removeObserver{}
        }
    }
    //기본 상태 설정
    private fun basicStatus()
    {

        myInfoView.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                "teacher" -> {
                    binding.switchTeacher.isChecked = true
                    binding.switchStudent.isChecked = false
                    binding.switchAcademy.isChecked = false
                }
                "student" ->
                {
                    binding.switchStudent.isChecked = true
                    binding.switchTeacher.isChecked = false
                    binding.switchAcademy.isChecked = false
                }
                "academy" ->
                {
                    binding.switchAcademy.isChecked = true
                    binding.switchTeacher.isChecked = false
                    binding.switchStudent.isChecked = false
                }
            }

        }
        myInfoView.status.removeObserver { }

    }


}