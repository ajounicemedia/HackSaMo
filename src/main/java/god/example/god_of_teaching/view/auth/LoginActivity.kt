package god.example.god_of_teaching.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.ActivityLoginBinding
import god.example.god_of_teaching.view.nav.HomeActivity
import god.example.god_of_teaching.viewmodel.AuthViewModel

//로그인 액티비티
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

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
    //다른 액티비티로 이동
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
    //로그인 성공유무 관찰
    private fun observeLoginCheck()
    {
        authView.loginCheck.observe(this, Observer {check->
            if (check) {
                Log.d("로그인 관찰", "성공")
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {//인터넷 문제등 조건 상세히 추가 필요
                Log.d("로그인 관찰", "실패")
                Toast.makeText(this, "없는 이메일이거나 비밀번호를 잘못 입력하셨습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //뒤로 가기시 다이얼로그
    private fun setupOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
    //종료 다이얼로그
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("앱 종료")
            .setMessage("앱을 종료하시겠습니까?")
            .setPositiveButton("예") { _, _ -> finish() }
            .setNegativeButton("아니오", null)
            .show()
    }

}