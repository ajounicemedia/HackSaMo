package god.example.god_of_teaching.view.nav.mywishlist.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import god.example.god_of_teaching.view.nav.mywishlist.AcademyWishListFragment
import god.example.god_of_teaching.view.nav.mywishlist.StudentWishListFragment
import god.example.god_of_teaching.view.nav.mywishlist.TeacherWishListFragment


class WishListViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 3 // 탭 수에 맞게 설정
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TeacherWishListFragment()
            1 -> StudentWishListFragment()
            2 -> AcademyWishListFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}