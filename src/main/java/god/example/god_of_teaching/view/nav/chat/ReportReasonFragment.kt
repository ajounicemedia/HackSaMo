package god.example.god_of_teaching.view.nav.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentReportReasonBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.ChatViewModel

//신고 사유 적는 화면
@AndroidEntryPoint
class ReportReasonFragment: Fragment() {
    private var otherUid : String?=null
    private val chatView : ChatViewModel by viewModels()
    private var _binding: FragmentReportReasonBinding?=null


    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //신고하기 위한 유저 uid받아옴
        otherUid = arguments?.getString("other_uid")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  FragmentReportReasonBinding.inflate(inflater,container,false)
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        uploadReportReason()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰기본세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarReportReason,"신고 사유 입력",binding.toolbarTitleReportReason)
        NavigationUtil.handleBackPress(this,navController)
    }
    //신고사유 업로드
    private fun uploadReportReason()
    {
        binding.btnUploadReport.setOnClickListener {
            val reportReason = binding.etReportReason.text.toString()
            if(reportReason.isNotBlank())
            {

                Toast.makeText(context, "신고가 완료 되었습니다", Toast.LENGTH_SHORT).show()
                chatView.uploadReportReason(otherUid!!,reportReason) //신고후 뒤로가기
                val navController = findNavController()
                navController.popBackStack()
            }
            else
            {
                Toast.makeText(context, "신고 목록이 비어있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}