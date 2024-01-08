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
import com.example.god_of_teaching.databinding.FragmentChangePasswordBinding
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.viewmodel.MyInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

//닉네임 변경 프래그먼트와 xml합쳐야함
//비밀번호 변경 프래그먼트
//비밀번호 조건문 추가 해줘야함
@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding : FragmentChangePasswordBinding?=null
    private val binding get() = _binding!!
    private val myInfoView: MyInfoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
            changePassword()
        observeChangePassword()
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

        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarChangePassword,"비밀번호 변경",binding.toolbarTitleChangePassword)
        NavigationUtil.handleBackPress(this,navController)
    }
    //비밀번호 조건문 추가 해줘야함
    //비밀번호 변경
    private fun changePassword()
    {
        binding.btnChangePasswordChangePassword.setOnClickListener{
        val passwordCheck = binding.etInputNewPasswordChangePassword.text.toString()
        val password = binding.etCheckPasswordChangePassword.text.toString()
            if(passwordCheck!=password)
            {
                Toast.makeText(context,"동일하게 비밀번호를 입력하지 않으셨습니다",Toast.LENGTH_SHORT).show()
            }
            else if(password.isEmpty())
            {
                Toast.makeText(context,"비밀번호를 입력하지 않으셨습니다",Toast.LENGTH_SHORT).show()
            }
            else
            {
                myInfoView.changePassword(password)
                Toast.makeText(context,"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
    //비밀번호 변경 체크
    private fun observeChangePassword(){
        myInfoView.changePasswordCheck.observe(viewLifecycleOwner, Observer {check->
            if (check) {
                Log.d("비밀번호 변경 관찰", "성공")
                val navController = findNavController()
                navController.popBackStack()
            }
            else {
                Log.d("비밀번호 변경 관찰", "실패")
                Toast.makeText(context, "잘못된 비밀번호를 입력하셨습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

}