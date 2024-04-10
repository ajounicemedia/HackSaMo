package god.example.god_of_teaching.view.nav.myinfo.registermodify.academy.register


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
import god.example.god_of_teaching.viewmodel.AcademyViewModel

//학원에서 가르키는 과목 입력 받는 프래그먼트
@AndroidEntryPoint
class AcademySubjectFragment : Fragment() {
    private var _binding : FragmentTeacherChoiceSubjectBinding?=null
    private val binding get() = _binding!!
    private lateinit var mySubjectList : MutableList<String>
    private val academyView : AcademyViewModel by viewModels()
    //과목 리스트
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
    //기본세팅
    private fun basicSetting()
    {
        binding.btnNextChoiceSubjectTeacher.text="다음"
        binding.btnNextChoiceSubjectTeacher.setBackgroundResource(R.drawable.custom_button_theme)
        binding.btnNextChoiceSubjectTeacher.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_white))
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarChoiceSubject)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)// 기본 제목 표시를 비활성화
            setDisplayHomeAsUpEnabled(true)//뒤로가기 활성화
        }
        //binding.toolbarTitleChoiceSubject.text = "학원 등록하기"
        mySubjectList = mutableListOf()
        val navController = findNavController()
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    //과목 체크하는 코드
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
    //과목 업로드 후 다음 프래그먼트로 이동하는 코드
    private fun next()
    {
        binding.btnNextChoiceSubjectTeacher.setOnClickListener {
            if (mySubjectList.size > 0) {
                academyView.uploadSubject(mySubjectList)
                val navController = findNavController()
                navController.popBackStack()
                navController.navigate(R.id.academyIntroduceFragment)

            } else {
                Toast.makeText(context, "1개 이상의 과목을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }


}