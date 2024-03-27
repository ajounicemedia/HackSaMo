package god.example.god_of_teaching.view.nav.myinfo.registermodify.student.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentStudentIntroduceBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.StudentViewModel


//학생 소개 업로드하는 프래그먼트
@AndroidEntryPoint
class StudentIntroduceFragment : Fragment() {


    private var _binding : FragmentStudentIntroduceBinding?=null
    private val binding get() = _binding!!
    private val studentView : StudentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentIntroduceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        uploadIntroduce()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 설정
    private fun basicSetting()
    {
        val navController = findNavController()
        binding.toolbarTitleIntroduceStudent.text = "학생 등록하기"
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    //입력한 정보 업로드 및 다음 프래그먼트로 이동
    private fun uploadIntroduce()
    {
        binding.btnCompleteIntroduceStudent.setOnClickListener {
            val introduce = binding.etIntroduceStudent.text.toString()
            studentView.uploadStudentIntroduce(introduce)
            val navController = findNavController()
            val bundle = Bundle()
            bundle.putString("complete", "student")
            navController.popBackStack()
            navController.navigate(R.id.RegisterCompleteFragment,bundle)
        }
    }
}