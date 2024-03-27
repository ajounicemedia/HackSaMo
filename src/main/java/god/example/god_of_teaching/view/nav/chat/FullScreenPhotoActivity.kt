package god.example.god_of_teaching.view.nav.chat

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import god.example.god_of_teaching.databinding.ActivityFullscreenPhotoBinding

//사진 확대 액티비티
class FullScreenPhotoActivity: AppCompatActivity() {
    private lateinit var binding : ActivityFullscreenPhotoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenPhotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        val imageUrl = intent.getStringExtra("IMAGE_URL")
        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivFullscreen)
        // 뒤로 가기 콜백 설정
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

}