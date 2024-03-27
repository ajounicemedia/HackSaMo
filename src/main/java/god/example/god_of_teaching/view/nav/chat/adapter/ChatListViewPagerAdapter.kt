package god.example.god_of_teaching.view.nav.chat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import god.example.god_of_teaching.view.nav.chat.chatlist.AcademyChatListFragment
import god.example.god_of_teaching.view.nav.chat.chatlist.StudentChatListFragment
import god.example.god_of_teaching.view.nav.chat.chatlist.TeacherChatListFragment

class ChatListViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 3 // 탭 수에 맞게 설정
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TeacherChatListFragment()
            1 -> StudentChatListFragment()
            2 -> AcademyChatListFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    }