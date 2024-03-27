package god.example.god_of_teaching.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import god.example.god_of_teaching.databinding.ActivityJoinCompleteBinding
import god.example.god_of_teaching.view.nav.HomeActivity

//회원가입 완료 액티비티
class JoinCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        basicSetting()
        setupOnBackPressed()
    }
    //로그인 완료후 홈으로 이동
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
    //뒤로 가기시 다이얼로그 나오게
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