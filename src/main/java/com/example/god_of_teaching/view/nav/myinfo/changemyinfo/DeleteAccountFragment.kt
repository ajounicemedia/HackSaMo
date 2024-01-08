package com.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.god_of_teaching.databinding.FragmentReportReasonBinding
import com.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import com.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import com.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.view.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

//신고 사유 적는 화면
@AndroidEntryPoint
class DeleteAccountFragment: Fragment()  {

    private var _binding: FragmentReportReasonBinding?=null
    private val binding get() = _binding!!
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  FragmentReportReasonBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        binding.etReportReason.hint = "탈퇴 사유를 입력해주세요."
        uploadDeleteReason()
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
                Toast.makeText(context, "탈퇴 사유가 비었습니다.", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "탈퇴가 완료되었습니다.\n그동안 서비스를 이용해\n주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    //캐시삭제
                    deleteCache()
                    val navController = findNavController()
                    navController.popBackStack()
                    // 모든 프래그먼트를 종료하고 LoginActivity로 이동
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }
    //캐시 삭제 함수
    private suspend fun deleteCache()
    {
        coroutineScope.launch {
            AcademyDatastoreHelper.getInstance(requireContext()).clearDataStore()
            StudentDatastoreHelper.getInstance(requireContext()).clearDataStore()
            TeacherDataStoreHelper.getInstance(requireContext()).clearDataStore()
            UserDataStoreHelper.getInstance(requireContext()).clearDataStore()
            coroutineScope.cancel()
        }.join()

    }
}