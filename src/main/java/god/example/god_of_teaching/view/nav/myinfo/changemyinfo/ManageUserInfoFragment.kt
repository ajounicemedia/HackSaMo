package god.example.god_of_teaching.view.nav.myinfo.changemyinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentManageUserInfoBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil

//비밀번호 변경할지 닉변할지 선택하는 프래그먼트
class ManageUserInfoFragment : Fragment() {
    private var _binding: FragmentManageUserInfoBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentManageUserInfoBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goOtherFragment()
        basicSetting()

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
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarManageUserInfo,"내 정보 관리",binding.toolbarTitleManageUserInfo)
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //다르 프래그먼트로 이동
    private fun goOtherFragment()
    {
        val navController = findNavController()
        binding.btnChangeNicknameManageUserInfo.setOnClickListener {
            navController.navigate(R.id.inputPasswordForNicknameFragment)
        }
        binding.btnChangePasswordManageUserInfo.setOnClickListener {
            navController.navigate(R.id.inputPasswordForPasswordFragment)
        }
    }



}