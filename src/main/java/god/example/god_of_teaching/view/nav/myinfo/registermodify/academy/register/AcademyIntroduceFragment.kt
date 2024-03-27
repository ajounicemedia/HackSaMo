package god.example.god_of_teaching.view.nav.myinfo.registermodify.academy.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentAcademyIntroduceBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.AcademyViewModel

//학원 소개 입력 받는 프래그먼트
@AndroidEntryPoint
class AcademyIntroduceFragment : Fragment() {

    private var _binding : FragmentAcademyIntroduceBinding?=null
    private val binding get() = _binding!!
    private val academyView : AcademyViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentAcademyIntroduceBinding.inflate(inflater, container, false)
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
    //기본세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        binding.toolbarTitleIntroduceAcademy.text = "학원 등록하기"
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    //소개 업로드 및 다음 프래그먼트로 이동
    private fun next()
    {
        binding.btnCompleteIntroduceAcademy.setOnClickListener {
            val introduce = binding.etIntroduceAcademy.text.toString()
            academyView.uploadIntroduce(introduce)
            val navController = findNavController()
            val bundle = Bundle()
            bundle.putString("complete", "academy")
            navController.popBackStack()
            navController.navigate(R.id.RegisterCompleteFragment,bundle)
        }
    }

}