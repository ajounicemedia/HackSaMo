package god.example.god_of_teaching.view.nav.mywishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentMainWishListBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.mywishlist.adapter.WishListViewPagerAdapter


class MainWishListFragment : Fragment() {
    private var _binding: FragmentMainWishListBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainWishListBinding.inflate(inflater,container,false)
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
    //뷰기본세팅
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
        val viewPagerAdapter = WishListViewPagerAdapter(childFragmentManager, lifecycle)

        // 뷰페이저 설정
        val viewPager = binding.viewPager
        viewPager.adapter = viewPagerAdapter

        // 탭 레이아웃과 뷰페이저 연결
        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "선생님 찜목록"
                1 -> "학생 찜목록"
                2 -> "학원 찜목록"
                else -> ""
            }
        }.attach()
    }
}