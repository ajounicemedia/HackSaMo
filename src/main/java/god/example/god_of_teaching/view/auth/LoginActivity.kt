package god.example.god_of_teaching.view.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.ActivityLoginBinding
import god.example.god_of_teaching.view.nav.HomeActivity
import god.example.god_of_teaching.viewmodel.AuthViewModel

//로그인 액티비티
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    private val authView: AuthViewModel by viewModels()
    private val currentUser =Firebase.auth.currentUser
    private var emailInputCheck = false
    private var passwordInputCheck = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupOnBackPressed()
        uiSetting()

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
            }
        })
        authView.loginMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d("로그인 관찰", message)
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
    //ui 프로그래밍
    private fun uiSetting()
    {
        binding.layout.setOnTouchListener { _, _ ->
            binding.layout.clearFocus()
            true
        }
        //이메일 입력 부분
        binding.etInputEmailLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 입력 중
                if (s.toString().isNotEmpty()) {
                    emailInputCheck=true
                    binding.etInputEmailLogin.setBackgroundResource(R.drawable.custom_layout_border_written)
                    updateBtn()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etInputEmailLogin.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etInputEmailLogin.setBackgroundResource(R.drawable.custom_layout_border)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etInputEmailLogin.windowToken, 0)
            }
        }
        //이메일 입력 부분
        binding.etInputPasswordLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 입력 중
                if (s.toString().isNotEmpty()) {
                    passwordInputCheck=true
                    binding.etInputPasswordLogin.setBackgroundResource(R.drawable.custom_layout_border_written)
                    updateBtn()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etInputPasswordLogin.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etInputPasswordLogin.setBackgroundResource(R.drawable.custom_layout_border)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etInputPasswordLogin.windowToken, 0)
            }
        }

    }
    //다음 로그인 활성화
    private fun updateBtn()
    {
        if(emailInputCheck&&passwordInputCheck)
        {

            binding.btnLogin.isEnabled=true
            binding.btnLogin.setBackgroundResource(R.drawable.custom_button_theme)
            binding.btnLogin.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.neutral_white))
        }

    }

}