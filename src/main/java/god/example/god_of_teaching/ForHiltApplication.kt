package god.example.god_of_teaching

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
//dfg
@HiltAndroidApp
class ForHiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}