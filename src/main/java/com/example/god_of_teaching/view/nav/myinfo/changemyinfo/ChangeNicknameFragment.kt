package com.example.god_of_teaching.view.nav.myinfo.changemyinfo


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.databinding.FragmentChangeNicknameBinding
import com.example.god_of_teaching.viewmodel.MyInfoViewModel
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import dagger.hilt.android.AndroidEntryPoint

//비밀번호 변경 프래그먼트와 xml 합쳐야함
//닉네임 변경 프래그먼트
@AndroidEntryPoint
class ChangeNicknameFragment : Fragment() {
    private lateinit var nickname:String
    private var _binding: FragmentChangeNicknameBinding?=null
    private val binding get() = _binding!!
    private val myInfoView: MyInfoViewModel by viewModels()

    private var userDataStoreHelper: UserDataStoreHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangeNicknameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        userDataStoreHelper = context?.let { UserDataStoreHelper.getInstance(it) }
            changeNickname()

        observeChangeNicknameCheck()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarChangeNickname,"닉네임 변경",binding.toolbarTitleChangeNickname)
        NavigationUtil.handleBackPress(this,navController)
    }
    //닉네임 변경
    private fun changeNickname() {
        binding.btnChangeNicknameChangeNickname.setOnClickListener {
            nickname = binding.etInputNicknameChangeNickname.text.toString()
            if (nickname.isEmpty()) {
                Toast.makeText(context, "닉네임이 입력되지 않았습니다", Toast.LENGTH_SHORT).show()
            } else {
                myInfoView.changeNickname(nickname)
            }
        }
    }
    //닉네임변경체크
    private fun observeChangeNicknameCheck()
    {
        myInfoView.nicknameChangeCheck.observe(viewLifecycleOwner, Observer {check->
            if (check) {
                myInfoView.changeNicknameLocal(nickname)
                Log.d("닉네임 변경 관찰", "성공")
                Toast.makeText(context, "닉네임 변경에 성공했습니다", Toast.LENGTH_SHORT).show()
                val navController = findNavController()
               navController.popBackStack()
            }
            else {
                Log.d("닉네임 변경 관찰", "실패")
                Toast.makeText(context, "닉네임 변경에 실패했습니다. 똑같은 닉네임을 입력하셨는지 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        })
    }

}