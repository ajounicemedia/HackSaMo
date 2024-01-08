package com.example.god_of_teaching.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.god_of_teaching.databinding.ActivityFindPasswordBinding
import com.example.god_of_teaching.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
//비밀번호 찾는 액티비티
@AndroidEntryPoint
class FindPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordBinding
    private val authView: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        sendEmail()
        observeSendEmail()
    }
    //이메일 코드 이메일 조건 걸어줘야함
    private fun sendEmail()
    {
        binding.btnSendEmailFindPassword.setOnClickListener()
        {
            val emailAddress = binding.etEmailFindPassword.text.toString()
            authView.sendEmail(emailAddress)
        }
    }

    //이메일 전송 됐는지 체크
    private fun observeSendEmail()
    {
        authView.emailCheck.observe(this, Observer{ emailGone ->
            if(emailGone)
            {
                Toast.makeText(this,"이메일이 발송 되었습니다 이메일을 확인해주세요",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this,"존재하지 않는 이메일이거나 알 수 없는 오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
            }
                }
        )
    }
}