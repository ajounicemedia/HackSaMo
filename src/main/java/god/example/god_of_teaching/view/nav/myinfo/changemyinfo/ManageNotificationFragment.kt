package god.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentManageNotificationBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil

//알림 온오프 프래그먼트
@AndroidEntryPoint
class ManageNotificationFragment : Fragment(){
    private var _binding: FragmentManageNotificationBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentManageNotificationBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        manageNotification()
        basicStatus()
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        val navController = findNavController()
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarManageNotification,"알림 설정",binding.toolbarTitleManageNotification)
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //알림 상태 관리
    private fun manageNotification()
    {
        binding.switchNotification.setOnCheckedChangeListener { buttonView, isChecked ->

                val intent = Intent().apply {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                }
                startActivity(intent)



        }
    }
    //기존에 스위치 설정
    private fun basicStatus()
    {
        binding.switchNotification.isChecked = isNotificationsEnabled()
    }

    //알람온오프확인
    private fun isNotificationsEnabled(): Boolean {
        val context = context ?: return false
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}