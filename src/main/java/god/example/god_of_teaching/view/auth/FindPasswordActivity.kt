package god.example.god_of_teaching.view.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.ActivityFindPasswordBinding
import god.example.god_of_teaching.viewmodel.AuthViewModel

//비밀번호 찾는 액티비티
@AndroidEntryPoint
class FindPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordBinding
    private val authView: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        uiSetting()
        setContentView(view)
        sendEmail()
        observeSendEmail()
    }

    //이메일 전송 코드
    private fun sendEmail() {
        binding.btnSendEmailFindPassword.setOnClickListener()
        {
            binding.etEmailFindPassword.clearFocus()
            val emailAddress = binding.etEmailFindPassword.text.toString()
            if(emailAddress.isEmpty())
            {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(!isValidEmail(emailAddress))
            {
                Toast.makeText(this, "유효하지 않은 이메일 형식입니다.", Toast.LENGTH_SHORT).show()
            }
            else if (emailAddress.isNotEmpty()&&isValidEmail(emailAddress)) {
                authView.sendEmail(emailAddress)
            }

        }
    }

    //이메일 전송 됐는지 관찰
    private fun observeSendEmail() {
        authView.emailCheck.observe(this) { result ->
            val (isSuccessful, errorMessage) = result
            if (isSuccessful) {
                // 성공 처리, 예: 성공 메시지 보여주기
                Toast.makeText(this,"이메일이 발송 되었습니다 이메일을 확인해주세요",Toast.LENGTH_SHORT).show()
            } else {
                // 실패 처리, 예: 오류 메시지 보여주기
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //ui설정
    private fun uiSetting() {
        //툴바 설정
        this.setSupportActionBar(binding.toolbarFindPassword)
        this.supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)// 기본 제목 표시를 비활성화
            setDisplayHomeAsUpEnabled(true)//뒤로가기 활성화
        }



        binding.etEmailFindPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 입력 중
                if (s.toString().isNotEmpty()) {
                    binding.etEmailFindPassword.setBackgroundResource(R.drawable.custom_layout_border_written)
                    binding.btnSendEmailFindPassword.isEnabled=true
                    binding.btnSendEmailFindPassword.setBackgroundResource(R.drawable.custom_button_theme)
                    binding.btnSendEmailFindPassword.setTextColor(ContextCompat.getColor(this@FindPasswordActivity, R.color.neutral_white))
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etEmailFindPassword.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etEmailFindPassword.setBackgroundResource(R.drawable.custom_layout_border)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etEmailFindPassword.windowToken, 0)
            }
        }
        binding.layout.setOnTouchListener { _, _ ->
            binding.etEmailFindPassword.clearFocus()
            true
        }
    }

    //왼쪽 위 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // 뒤로 가기 버튼 선택 시
        if (item.itemId == android.R.id.home) {
            // 현재 액티비티 종료, 뒤로 가기
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    //이메일 형식 체크
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}