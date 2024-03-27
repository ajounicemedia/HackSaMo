package god.example.god_of_teaching.model.`object`.util

import android.app.Activity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.NavController

object MenuUtil {
        fun setupMenu(activity: Activity, navController: NavController) {

        val menuHost: MenuHost = activity as MenuHost
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // 여기에 메뉴 생성 로직 추가
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        navController.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        })
    }
}