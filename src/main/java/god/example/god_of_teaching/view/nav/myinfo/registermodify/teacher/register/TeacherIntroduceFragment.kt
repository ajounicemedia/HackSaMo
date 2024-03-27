package god.example.god_of_teaching.view.nav.myinfo.registermodify.teacher.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentTeacherIntroduceBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.TeacherViewModel

//선생님 소개 업로드하는 프래그먼트
@AndroidEntryPoint
class TeacherIntroduceFragment : Fragment() {

    private var _binding : FragmentTeacherIntroduceBinding?=null
    private val binding get() = _binding!!
    private val teacherView : TeacherViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentTeacherIntroduceBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        next()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        binding.toolbarTitleIntroduceTeacher.text = "선생님 등록하기"
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    //입력한 정보 업로드 및 다음 프래그먼트로 이동
    private fun next()
    {
            binding.btnCompleteIntroduceTeacher.setOnClickListener {
            val introduce = binding.etIntroduceTeacher.text.toString()
            teacherView.uploadTeacherIntroduce(introduce)
            val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("complete", "teacher")
            navController.popBackStack()
            navController.navigate(R.id.RegisterCompleteFragment,bundle)
        }
    }


}