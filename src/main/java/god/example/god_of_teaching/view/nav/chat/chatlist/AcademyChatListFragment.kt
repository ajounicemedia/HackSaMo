package god.example.god_of_teaching.view.nav.chat.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentChatListBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.chat.adapter.ChatListAdapter
import god.example.god_of_teaching.viewmodel.ChatViewModel
import god.example.god_of_teaching.viewmodel.StudentToAcademyChatViewModel

@AndroidEntryPoint
class AcademyChatListFragment: Fragment() {
    private var _binding: FragmentChatListBinding?=null
    private val binding get() = _binding!!
    private val studentToAcademyChatViewModel : StudentToAcademyChatViewModel by viewModels()
    private val chatView : ChatViewModel by viewModels()
    private lateinit var academyChatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentToAcademyChatViewModel.getChatList()
        _binding = FragmentChatListBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        createAdapter()
        observeChatMessages()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본세팅
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
    private fun createAdapter()
    {
        binding.rvChatList.layoutManager = LinearLayoutManager(context)
        academyChatListAdapter = ChatListAdapter(
            {
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("other_name", it.otherName)
                bundle.putString("other_uid", it.otherUid)
                bundle.putString("my_chat_type",it.myChatType)

                navController.navigate(R.id.studentToAcademyChatRoomFragment, bundle)
                //채팅 리스트 업데이트
                if (it.newMessage == true) {
                    it.otherUid?.let { it1 -> studentToAcademyChatViewModel.changeMessageStatus(it1) }
                }
            }
            ,
            {
                chatView.checkBlackNotify(it.otherUid!!)
            },
            {
                chatView.checkBlackList(it.otherName!!)
            }
        )
        binding.rvChatList.adapter = academyChatListAdapter
    }
    private fun observeChatMessages() {
        studentToAcademyChatViewModel.chatList?.observe(viewLifecycleOwner, Observer { chatList ->
            //어댑터 다시 생성해야할거 대비
//            if (!::chatMessageAdapter.isInitialized) {
//                createAdapter()
//            }
            academyChatListAdapter.submitList(chatList)
            //linearLayoutManager.smoothScrollToPosition(binding.rvChatList,null,chatMessageAdapter.itemCount)
        })
    }
}