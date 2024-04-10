package god.example.god_of_teaching.view.nav.myinfo.registermodify.student.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentTeacherChoiceSubjectBinding
import god.example.god_of_teaching.model.`object`.data.SubjectData
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.StudentViewModel

//학생이 선택한 과목 업로드하는 코드
@AndroidEntryPoint
class StudentSubjectFragment : Fragment() {
    private var _binding : FragmentTeacherChoiceSubjectBinding?=null
    private val binding get() = _binding!!
    private lateinit var mySubjectList : MutableList<String>
    private val studentView : StudentViewModel by viewModels()
    private val checkBoxIdList = SubjectData.subjectList
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentTeacherChoiceSubjectBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        checkSubject()
        next()

        ///왼쪽 위 뒤로가기
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val navController = findNavController()
                navController.popBackStack()
                return true
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        binding.btnNextChoiceSubjectTeacher.text="다음"
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarChoiceSubject)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)// 기본 제목 표시를 비활성화
            setDisplayHomeAsUpEnabled(true)//뒤로가기 활성화
        }
        //binding.toolbarTitleChoiceSubject.text = "학생 등록하기"
        mySubjectList = mutableListOf()
        val navController = findNavController()
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    //과목 체크하기
    private fun checkSubject()
    {
        for (id in checkBoxIdList) {
            val checkBox = binding.root.findViewById<CheckBox>(id)
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    mySubjectList.add(buttonView.text.toString())
                    binding.btnNextChoiceSubjectTeacher.isEnabled=true
                    binding.btnNextChoiceSubjectTeacher.setBackgroundResource(R.drawable.custom_button_theme)
                    binding.btnNextChoiceSubjectTeacher.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_white))
                } else {
                    mySubjectList.remove(buttonView.text.toString())
                    binding.btnNextChoiceSubjectTeacher.isEnabled=false
                    binding.btnNextChoiceSubjectTeacher.setBackgroundResource(R.drawable.custom_disable_button_theme)
                    binding.btnNextChoiceSubjectTeacher.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_60))
                }
                Log.d("리스트 체크", mySubjectList.toString())
            }
        }
    }
    //선택한 데이터 업로드 및 다음 프래그먼트로 이동하는 함수
    private fun next()
    {
        binding.btnNextChoiceSubjectTeacher.setOnClickListener {
            if (mySubjectList.size > 0) {

                studentView.uploadStudentSubject(mySubjectList)
                val navController = findNavController()
                navController.popBackStack()
                navController.navigate(R.id.studentIntroduceFragment)
            } else {
                Toast.makeText(context, "1개 이상의 과목을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}