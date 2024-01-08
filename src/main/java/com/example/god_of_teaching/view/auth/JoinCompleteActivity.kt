package com.example.god_of_teaching.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.god_of_teaching.databinding.ActivityJoinCompleteBinding
import com.example.god_of_teaching.view.nav.HomeActivity
import com.example.god_of_teaching.viewmodel.AuthViewModel

//회원가입 완료 액티비티
class JoinCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinCompleteBinding
    private val authView: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        basicSetting()
        setupOnBackPressed()
    }
    private fun loginComplete()
    {
        binding.btnLoginCompleteJoin.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        binding = ActivityJoinCompleteBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        loginComplete()
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

}