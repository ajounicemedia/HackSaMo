package com.example.god_of_teaching.view.nav.chat.chatlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentChatListBinding
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.view.nav.chat.PreferenceUtils
import com.example.god_of_teaching.view.nav.chat.adapter.ChatListAdapter
import com.example.god_of_teaching.viewmodel.StudentChatViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentChatListFragment : Fragment() {
    private var _binding: FragmentChatListBinding?=null
    private val binding get() = _binding!!
    private val studentChatView : StudentChatViewModel by viewModels()
    private lateinit var studentChatListAdapter: ChatListAdapter
    //sharedPreferences사용하기위해서 여기 뷰랑 연결되어 있을 때만 사용 가능하니까 주의하자
    private lateinit var preferenceUtils: PreferenceUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentChatView.getStudentChatList()
        _binding = FragmentChatListBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Shared Preferences 인스턴스 생성 및 PreferenceUtils 초기화
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        preferenceUtils = PreferenceUtils(sharedPreferences)
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
        studentChatListAdapter = ChatListAdapter(
            {
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("other_name", it.otherName)
                bundle.putString("other_uid", it.otherUid)
                navController.navigate(R.id.studentChatRoomFragment, bundle)
                //채팅 리스트 업데이트
                if (it.newMessage == true) {
                    it.otherUid?.let { it1 -> studentChatView.changeMessageStatus(it1) }
                }
            }
            ,
            {
                preferenceUtils.reportCheck(it.otherUid!!)
            },
            {
                preferenceUtils.isBlackListed(it.otherUid!!)
            }
        )
        binding.rvChatList.adapter = studentChatListAdapter
    }
    private fun observeChatMessages() {
        studentChatView.studentChatList?.observe(viewLifecycleOwner, Observer { chatList ->
            //어댑터 다시 생성해야할거 대비
//            if (!::chatMessageAdapter.isInitialized) {
//                createAdapter()
//            }
            studentChatListAdapter.submitList(chatList)
            //linearLayoutManager.smoothScrollToPosition(binding.rvChatList,null,chatMessageAdapter.itemCount)
        })
    }
}