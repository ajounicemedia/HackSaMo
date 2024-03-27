package god.example.god_of_teaching.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.ActivityJoinBinding
import god.example.god_of_teaching.viewmodel.AuthViewModel

//회원 가입 액티비티
@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {
    private lateinit var nickname: String
    private lateinit var email: String
    private lateinit var binding : ActivityJoinBinding
    private var nicknameCheck : Boolean? = null
    private var emailCheck : Boolean?= null
    private val authView: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        checkNicknameSame()
        checkEmailSame()
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
            else if(password.length<6||password.length>16)
            {
                Toast.makeText(this, "비밀번호는 6글자 이상 16글자 이하여야합니다.", Toast.LENGTH_SHORT).show()
            }
            else if(nicknameCheck==null||nicknameCheck==false)
            {
                Toast.makeText(this, "닉네임 조건 확인이 완료 되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
            else if(emailCheck==null||emailCheck==false)
            {
                Toast.makeText(this, "이메일 조건 확인이 완료 되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
            else if (password == passwordCheck)//이메일 조건 추가해야됌
            {
                authView.join(email, password, nickname)
            } else
            {
                Toast.makeText(this, "비빌번호과 비밀번호 체크가 일치 하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }

    }
    //이메일 중복 체크
    private fun checkEmailSame()
    {
        binding.btnEmailSameCheck.setOnClickListener {
            email = binding.etEmailJoin.text.toString()
            if(!isValidEmail(email))
            {
                Toast.makeText(this, "이메일 형식이 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                authView.sameEmailCheck(email)
            }
        }
    }
    //닉네임 중복 체크
    private fun checkNicknameSame()
    {

        binding.btnNicknameSameCheck.setOnClickListener {
            nickname = binding.etNicknameJoin.text.toString()
            if(nickname.isEmpty())
            {
                Toast.makeText(this,"닉네임을 입력하지 않았습니다.",Toast.LENGTH_SHORT).show()
            }
            else if(nickname.length>11)
            {
                Toast.makeText(this,"닉네임은 10자 이하로 설정 가능합니다.",Toast.LENGTH_SHORT).show()
            }
            else
            {
                authView.checkSameNickname(nickname)
            }

        }
    }
    //회원가입, 닉네임 중복, 이메일 중복 관찰
    private fun observe()
    {
        authView.joinCheck.observe(this) {check->
            if(check)
            {
                Toast.makeText(this,"회원가입이 성공했습니다!",Toast.LENGTH_SHORT).show()
                authView.insertUserDataLocal(nickname,email)
                startActivity(Intent(this, JoinCompleteActivity::class.java))
                finish()
            }
            else
            {
                Log.d("버그","JoinActivity에서 회원가입 체크 실패하는 중 ")
                Toast.makeText(this,"인터넷 문제로 회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        authView.nicknameSameCheck.observe(this){ nicknameSameCheck ->
            if(nicknameSameCheck==true)
            {
                nicknameCheck =true
                Toast.makeText(this,"사용 가능한 닉네임입니다",Toast.LENGTH_SHORT).show()
            }
            else
            {
                nicknameCheck =false
                Toast.makeText(this,"중복되어 사용이 불가능한 닉네임입니다.",Toast.LENGTH_SHORT).show()
            }
        }
        authView.sameEmailCheck.observe(this){emailSameCheck->
            if(emailSameCheck)
            {
                emailCheck = true
                Toast.makeText(this,"사용 가능한 이메일입니다",Toast.LENGTH_SHORT).show()
            }
            else
            {
                emailCheck =false
                Toast.makeText(this,"중복되어 사용이 불가능한 이메일입니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //이메일 형식 체크
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}