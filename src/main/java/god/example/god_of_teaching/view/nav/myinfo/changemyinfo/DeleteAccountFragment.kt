package god.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentReportReasonBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.view.auth.LoginActivity
import god.example.god_of_teaching.viewmodel.AuthViewModel


//신고 사유 적는 화면 삭제됐는지 체크해야함
@AndroidEntryPoint
class DeleteAccountFragment: Fragment()  {

    private var _binding: FragmentReportReasonBinding?=null
    private val binding get() = _binding!!
    private val authView: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  FragmentReportReasonBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        binding.etReportReason.hint = "탈퇴 사유를 입력해주세요."
        binding.btnUploadReport.text = "탈퇴하기"
        uploadDeleteReason()
        observeDeleteAccount()
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
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarReportReason,"계정 탈퇴 사유 입력",binding.toolbarTitleReportReason)
        NavigationUtil.handleBackPress(this,navController)
    }
    //탈퇴사유 업로드
    private fun uploadDeleteReason()
    {
        binding.btnUploadReport.setOnClickListener {
            val deleteReason = binding.etReportReason.text.toString()
            if(deleteReason.isNotEmpty())
            {
                authView.deleteAccount(deleteReason)

            }
            else
            {
                Toast.makeText(context, "탈퇴 사유가 비었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //계정 탈퇴 체크
    private fun observeDeleteAccount()
    {
        authView.accountDeleteCheck.observe(viewLifecycleOwner, Observer { check ->
                if(check)
                {
                    Toast.makeText(context, "탈퇴가 완료되었습니다.\n그동안 서비스를 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
                    //캐시삭제
                    //deleteCache()
//                    val navController = findNavController()
//                    navController.popBackStack()
                    // 모든 프래그먼트를 종료하고 LoginActivity로 이동
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(context, "오류가 발생하여 탈퇴가 완료되지 않았습니다.\n인터넷 연결 등을 확인 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
                }

        })
    }
    //캐시 삭제 함수
//    private suspend fun deleteCache()
//    {

//
//    }
}