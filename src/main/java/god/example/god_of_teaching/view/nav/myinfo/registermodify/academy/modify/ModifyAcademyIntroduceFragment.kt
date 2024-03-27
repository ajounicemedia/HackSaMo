package god.example.god_of_teaching.view.nav.myinfo.registermodify.academy.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentTeacherIntroduceBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.AcademyViewModel

@AndroidEntryPoint
class ModifyAcademyIntroduceFragment : Fragment()  {

    private var _binding : FragmentTeacherIntroduceBinding?=null
    private val binding get() = _binding!!
    private val academyView: AcademyViewModel by viewModels()

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

            academyView.readIntroduce.observe(viewLifecycleOwner){ existingItem  ->
                    binding.etIntroduceTeacher.setText(existingItem)
            }

    }
    //변경 완료
    private fun next()
    {
        binding.btnCompleteIntroduceTeacher.setOnClickListener {
            val introduce = binding.etIntroduceTeacher.text.toString()
            academyView.uploadIntroduce(introduce)
            academyView.changeAcademyStatus()
            val navController = findNavController()
            navController.popBackStack()
        }
    }

}