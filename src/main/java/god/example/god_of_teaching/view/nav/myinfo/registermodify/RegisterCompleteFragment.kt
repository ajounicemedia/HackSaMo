package god.example.god_of_teaching.view.nav.myinfo.registermodify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentRegisterCompleteBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.AcademyViewModel
import god.example.god_of_teaching.viewmodel.AuthViewModel
import god.example.god_of_teaching.viewmodel.StudentViewModel
import god.example.god_of_teaching.viewmodel.TeacherViewModel

//등록하기 완료 프래그먼트
@AndroidEntryPoint
class RegisterCompleteFragment : Fragment() {

    private var _binding : FragmentRegisterCompleteBinding?=null
    private val binding get() = _binding!!
    private var completeItem : String?=null
    private val teacherView : TeacherViewModel by viewModels()
    private val studentView : StudentViewModel by viewModels()
    private val academyView : AcademyViewModel by viewModels()
    private val authView : AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        completeItem = arguments?.getString("complete")
        completeItem?.let { Log.d("123123123", it) }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        _binding = FragmentRegisterCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        NavigationUtil.handleBackPress(this,navController)
        binding.btnCompleteComplete.setOnClickListener {
            navController.popBackStack()
            complete()
        }
    }
    private fun complete()
    {

        when(completeItem)
        {
            "teacher" ->
            {
                teacherView.registerCompleteTeacher()

            }
            "student" ->
            {
                studentView.registerStudentComplete()
            }
            "academy" ->
            {
                academyView.changeAcademyStatus()
            }

        }
    }

}