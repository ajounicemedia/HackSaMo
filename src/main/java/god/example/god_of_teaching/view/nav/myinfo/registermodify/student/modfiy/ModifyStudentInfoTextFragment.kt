package god.example.god_of_teaching.view.nav.myinfo.registermodify.student.modfiy

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
import god.example.god_of_teaching.viewmodel.StudentViewModel

@AndroidEntryPoint
class ModifyStudentInfoTextFragment : Fragment(){
    private var _binding: FragmentModifyMyInfoTextBinding?=null
    private val binding get() = _binding!!
    private val studentView: StudentViewModel by viewModels()
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
        modifiedItem?.let { modifyStudentInfo(it) }
        basicSetting()
        modifiedArticle()
    }
    private fun basicSetting()
    {
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixTextInfo,
            "$modifiedItem 수정",binding.toolbarTitleFixTextInfo)
        NavigationUtil.handleBackPress(this,navController)
    }
    //ui에 기존 것들 반영
    private fun modifiedArticle()
    {
        when(modifiedItem)
        {
            // 여기 있는 코루틴 위에 선언 해줬다 //닉네임
//            "닉네임" ->   {
//
//            }
            // 여기 있는 코루틴 위에 선언 해줬다 //한줄소개
            "한줄소개"->

                studentView.readDes.observe(viewLifecycleOwner) { value ->
                        binding.etChangeDataModifyMyInfoText.setText(value)
            }
        }

    }
    //수정 반영
    private fun modifyStudentInfo(modifyItem: String)
    {
        binding.btnCompleteModifyMyInfoText.setOnClickListener{
            val modifyName = binding.etChangeDataModifyMyInfoText.text.toString()

            studentView.modifyStudentInfo(modifyItem, modifyName)
            val navController = findNavController()
            navController.popBackStack()
        }
    }

}