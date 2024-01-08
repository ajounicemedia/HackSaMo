package com.example.god_of_teaching.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.god_of_teaching.databinding.ActivityJoinBinding
import com.example.god_of_teaching.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

//회원 가입 액티비티 동일한 이메일 일 때 토스트 메세지, 에러 메세지 구분하는 작업 필요
@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {
    private lateinit var nickname: String
    private lateinit var email: String
    private lateinit var binding : ActivityJoinBinding
    private val authView: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
            join()
        observe()
    }
    //회원 가입
    private fun join() {
        binding.btnNext.setOnClickListener {
            email = binding.etEmailJoin.text.toString()
            nickname = binding.etNicknameJoin.text.toString() // 중복 불가 코드 추가
            val password = binding.etPasswordJoin.text.toString()
            val passwordCheck = binding.etPasswordCheckJoin.text.toString()


            if (email.isBlank() || password.isBlank() || passwordCheck.isBlank() || nickname.isBlank()) {
                Toast.makeText(this, "모든 항목을 다 입력해야합니다", Toast.LENGTH_SHORT).show()
            }


            if (password == passwordCheck)//이메일 조건 추가해야됌
            {
                authView.join(email, password, nickname)

            } else {
                Toast.makeText(this, "비빌번호과 비밀번호 체크가 일치 하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }

    }
    //회원가입 체크
    private fun observe()
    {
        authView.joinCheck.observe(this) {check->
            if(check)
            {
                Toast.makeText(this,"회원가입이 성공했습니다!",Toast.LENGTH_SHORT).show()
                authView.insertNicknameEmailLocal(nickname,email)
                startActivity(Intent(this, JoinCompleteActivity::class.java))
                finish()
            }
            else
            {
                Log.d("버그","JoinActivity에서 회원가입 체크 실패하는 중 ")
                Toast.makeText(this,"인터넷 문제로 회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }



}