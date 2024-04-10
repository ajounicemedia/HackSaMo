package god.example.god_of_teaching.model.`object`.util

import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import god.example.god_of_teaching.R

//뷰에서 툴바 설정해주는 오브젝트
object ToolbarUtil {
    fun setupToolbar(
        activity: AppCompatActivity, toolbar: Toolbar,
        title:String,
        titleTextView: TextView
    ) {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)// 기본 제목 표시를 비활성화
            setDisplayHomeAsUpEnabled(true)//뒤로가기 활성화
        }
        titleTextView.text = title
        titleTextView.setTextColor(ContextCompat.getColor(activity, R.color.neutral_100))
    }
}
