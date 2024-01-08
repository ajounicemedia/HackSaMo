package com.example.god_of_teaching.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.god_of_teaching.databinding.ActivityLoginBinding
import com.example.god_of_teaching.view.nav.HomeActivity
import com.example.god_of_teaching.viewmodel.AuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

//로그인 액티비티
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    //private lateinit var authViewModel: AuthViewModel
    private val authView: AuthViewModel by viewModels()
    private val currentUser =Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setupOnBackPressed()


        //현재 유저 상태 체크
        if(currentUser!=null)
        {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        observeLoginCheck()
        login()
        goOtherActivity()


    }
    private fun goOtherActivity()
    {
        //회원가입으로 이동
        binding.btnJoin.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }
        binding.btnFindPassword.setOnClickListener {
            startActivity(Intent(this, FindPasswordActivity::class.java))
        }
    }
    //로그인
    private fun login()
    {
        binding.btnLogin.setOnClickListener {
            val email = binding.etInputEmailLogin.text.toString()
            val password = binding.etInputPasswordLogin.text.toString()

            if (email.isEmpty()|| password.isEmpty()) {
                Toast.makeText(this, "비밀번호나 아이디가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            } else {
                authView.login(email, password)
            }
        }

    }
    //로그인 체크
    private fun observeLoginCheck()
    {
        authView.loginCheck.observe(this, Observer {check->
            if (check) {
                Log.d("로그인 관찰", "성공")
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Log.d("로그인 관찰", "실패")
                Toast.makeText(this, "없는 이메일이거나 비밀번호를 잘못 입력하셨습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //뒤로 가기 동작하는 코드
    private fun setupOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("앱 종료")
            .setMessage("앱을 종료하시겠습니까?")
            .setPositiveButton("예") { _, _ -> finish() }
            .setNegativeButton("아니오", null)
            .show()
    }
    //정보 입력 체크
//    private fun observeInfoCheck()
//    {
//        authViewModel.loginInfoCheck.observe(this, Observer {check->
//            //정보 입력 안 했을시 정보 입력하는 곳으로
//            if(check)
//            {
//                startActivity(Intent(this, InputInfoActivity::class.java))
//            }
//            else//정보 입력 다 됐으면
//            {
//                startActivity(Intent(this, HomeActivity::class.java))
//            }
//        })
//    }

    //정보 입력 체크
//    private fun currentUserCheck()
//    {
//        authViewModel.currentUserCheck.observe(this, Observer {check->
//            //정보 입력 안 했을시 정보 입력하는 곳으로
//            if(check)
//            {
//                startActivity(Intent(this, InputInfoActivity::class.java))
//            }
//            else//정보 입력 다 됐으면
//            {
//                startActivity(Intent(this, HomeActivity::class.java))
//            }
//        })
//    }
}