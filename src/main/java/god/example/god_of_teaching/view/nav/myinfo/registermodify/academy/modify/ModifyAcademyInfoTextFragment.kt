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
import god.example.god_of_teaching.databinding.FragmentModifyMyInfoTextBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.AcademyViewModel

@AndroidEntryPoint
class ModifyAcademyInfoTextFragment : Fragment(){
    private var _binding: FragmentModifyMyInfoTextBinding?=null
    private val binding get() = _binding!!
    private val academyView: AcademyViewModel by viewModels()
    private var modifiedItem : String?=null


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modifiedItem = arguments?.getString("modified_item")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {

        _binding = FragmentModifyMyInfoTextBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        modifiedArticle()
        modifiedItem?.let { modifyAcademyInfo(it) }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        academyView.readDes.observe(viewLifecycleOwner){ existingItem->
                    binding.etChangeDataModifyMyInfoText.setText(existingItem)
        }
    }
    //수정 반영
    private fun modifyAcademyInfo(modifyItem: String)
    {
        binding.btnCompleteModifyMyInfoText.setOnClickListener{
            val modifyName = binding.etChangeDataModifyMyInfoText.text.toString()

            academyView.modifyAcademyInfo(modifyItem, modifyName)
            val navController = findNavController()
            navController.popBackStack()
        }
    }


}