package god.example.god_of_teaching.view.nav.myinfo.registermodify.teacher.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentModifyMyInfoTextBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.TeacherViewModel


//텍스트에 있는 선생님 정보 수정하기 위한 프래그먼트
@AndroidEntryPoint
class ModifyTeacherInfoTextFragment : Fragment() {

    private var _binding: FragmentModifyMyInfoTextBinding?=null
    private val binding get() = _binding!!
    private val teacherView: TeacherViewModel by viewModels()
    private var modifiedItem : String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         modifiedItem = arguments?.getString("modified_item")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentModifyMyInfoTextBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modifiedItem?.let { modifyTeacherInfo(it) }
        basicSetting()
        modifiedArticle()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixTextInfo,
            "$modifiedItem 수정",binding.toolbarTitleFixTextInfo)
        NavigationUtil.handleBackPress(this,navController)
    }
    //어떤 항목 수정할지 데이터 받기
    private fun modifiedArticle()
    {
        when(modifiedItem)
        {
            // 여기 있는 코루틴 위에 선언 해줬다
//            "닉네임" ->
//                {
//            }
            // 여기 있는 코루틴 위에 선언 해줬다
            "한줄소개"->
                teacherView.readDes.observe(viewLifecycleOwner){ value ->
                        binding.etChangeDataModifyMyInfoText.setText(value)
                }

        }

    }
    //선택한 정보 변경
        private fun modifyTeacherInfo(modifyItem: String)
        {
            binding.btnCompleteModifyMyInfoText.setOnClickListener{
            val modifyName = binding.etChangeDataModifyMyInfoText.text.toString()

            teacherView.modifyTeacherInfo(modifyItem, modifyName)
                val navController = findNavController()
                navController.popBackStack()
           }
        }

}