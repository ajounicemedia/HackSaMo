package god.example.god_of_teaching.view.nav.myinfo.registermodify.student.modfiy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentTeacherChoiceSubjectBinding
import god.example.god_of_teaching.model.`object`.data.SubjectData
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.StudentViewModel


//학생과목수정
@AndroidEntryPoint
class ModifyStudentSubjectFragment : Fragment() {
    private var _binding : FragmentTeacherChoiceSubjectBinding?=null
    private val binding get() = _binding!!
    private lateinit var mySubjectList : MutableList<String>
    private val studentView : StudentViewModel by viewModels()
    private val checkBoxIdList = SubjectData.subjectList
    private lateinit var mySubject : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySubjectList = mutableListOf()
        getExistingItem()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentTeacherChoiceSubjectBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getExistingItem()

        basicSetting()
        checkSubject()
        uploadStudentSubjectData()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {

        binding.btnNextChoiceSubjectTeacher.isEnabled=true
        binding.btnNextChoiceSubjectTeacher.setBackgroundResource(R.drawable.custom_button_theme)
        binding.btnNextChoiceSubjectTeacher.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_white))
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarChoiceSubject,"과목 변경",binding.toolbarTitleChoiceSubject)
        NavigationUtil.handleBackPress(this,navController)
    }
    //과목 선택하기
    private fun checkSubject()
    {
        for (id in checkBoxIdList) {
            val checkBox = binding.root.findViewById<CheckBox>(id)
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (!mySubjectList.contains(buttonView.text.toString())) {
                        mySubjectList.add(buttonView.text.toString())
                    }
                } else {
                    mySubjectList.remove(buttonView.text.toString())
                }
                Log.d("리스트 체크", mySubjectList.toString())
            }
        }
    }
    //과목 선택하고 원래 프래그먼트로 돌아가기
    private fun uploadStudentSubjectData()
    {
        binding.btnNextChoiceSubjectTeacher.setOnClickListener {
            if (mySubjectList.size > 0) {
                studentView.modifyStudentInfoArray("과목", mySubjectList)
                val navController = findNavController()
                navController.popBackStack()

            } else {
                Toast.makeText(context, "1개 이상의 과목을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }
    //원래 선택했던 과목들 불러오기
    private fun getExistingItem()
    {
            studentView.readSubject.observe(viewLifecycleOwner){ value ->
                value?.let {
                    val items = it.split(",").map { it.trim() }   // 쉼표를 기준으로 분리
                    mySubjectList.addAll(items) // 내 과목에 추가
                }
                for (id in checkBoxIdList) {
                    val checkBox = binding.root.findViewById<CheckBox>(id)
                    if (mySubjectList.contains(checkBox.text.toString())) {
                        checkBox.isChecked = true
                    }
                }
        }
    }

}