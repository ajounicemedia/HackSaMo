package com.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentManageBlacklistBinding
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.view.nav.chat.PreferenceUtils
import com.google.android.material.bottomnavigation.BottomNavigationView


class ManageBlackListFragment : Fragment(){
    private var _binding: FragmentManageBlacklistBinding?=null
    private val binding get() = _binding!!
    private val blackList :List<String>?=null
    //sharedPreferences사용하기위해서 여기 뷰랑 연결되어 있을 때만 사용 가능하니까 주의하자
    private lateinit var preferenceUtils: PreferenceUtils
    private lateinit var blackListAdapter: BlackListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentManageBlacklistBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Shared Preferences 인스턴스 생성 및 PreferenceUtils 초기화
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        preferenceUtils = PreferenceUtils(sharedPreferences)
        basicSetting()
        setBlackList()
        createAdapter()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        val navController = findNavController()
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarBlacklist,"내 차단리스트",binding.toolbarTitleBlacklist)
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //로컬에 저장되어 있는 블랙리스트 배열에 추가
    private fun setBlackList()
    {
        val preferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val allEntries = preferences.all
        val blackListEntries = mutableListOf<String>()

        for ((key, value) in allEntries) {
            if (key.startsWith("blacklist")) {
                // 여기서 value를 리스트에 추가
                blackListEntries.add(value.toString())
            }
        }
    }
    //블랙리스트 어댑터 생성
    private fun createAdapter()
    {
        binding.rvBlacklist.layoutManager = LinearLayoutManager(context)
        blackListAdapter = blackList?.let {
            BlackListAdapter(
                it
            ) {
                preferenceUtils.removeBlackList(it)
            }
        }!!
    }
}