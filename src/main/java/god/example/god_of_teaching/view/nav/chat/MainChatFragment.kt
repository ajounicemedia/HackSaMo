package god.example.god_of_teaching.view.nav.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentChatBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.chat.adapter.ChatListViewPagerAdapter

//채팅 메인 프래그먼트
class MainChatFragment : Fragment() {
    private var _binding: FragmentChatBinding?=null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        createViewPager()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            if (navigationView.visibility == View.GONE) {
                navigationView.visibility = View.VISIBLE
            }
        }
        val navController = findNavController()
        NavigationUtil.setupBackNavigationHome(this, navController)
    }
    //뷰페이저 생성
    private fun createViewPager()
    {
        val viewPagerAdapter = ChatListViewPagerAdapter(childFragmentManager, lifecycle)

        // 뷰페이저 설정
        val viewPager = binding.viewPager
        viewPager.adapter = viewPagerAdapter

        // 탭 레이아웃과 뷰페이저 연결
        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "선생님 채팅 목록"
                1 -> "학생 채팅 목록"
                2 -> "학원 채팅 목록"
                else -> ""
            }
        }.attach()
    }

}