package god.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentManageBlacklistBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.ChatViewModel

@AndroidEntryPoint
class ManageBlackListFragment : Fragment(){
    private var _binding: FragmentManageBlacklistBinding?=null
    private val binding get() = _binding!!
    private var blackList: List<String>?=null

    private lateinit var blackListAdapter: BlackListAdapter
    private val chatView : ChatViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentManageBlacklistBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        blackList = chatView.getBlacklist()
        Log.d("BlackList", blackList.toString())
    }
    //블랙리스트 어댑터 생성
    private fun createAdapter()
    {
        binding.rvBlacklist.layoutManager = LinearLayoutManager(context)
        blackListAdapter = BlackListAdapter {
            //preferenceUtils.removeBlackList(it)
            chatView.removeBlackList(it)
            updateBlackList()
            //chatView.removeBlackList(it)
        }
        blackListAdapter.submitList(blackList)  // 초기 데이터 세트
        binding.rvBlacklist.adapter = blackListAdapter
    }
    //삭제했을 때 바로 업데이트
    private fun updateBlackList() {

        blackList = chatView.getBlacklist()
        blackListAdapter.submitList(blackList)  // 어댑터에 변경 사항을 전달
    }
}