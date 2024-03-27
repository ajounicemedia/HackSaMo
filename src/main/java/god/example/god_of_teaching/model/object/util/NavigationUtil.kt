package god.example.god_of_teaching.model.`object`.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import god.example.god_of_teaching.R

//네비게이션 관련 코드
object NavigationUtil {
        //뒤로가기시 홈
        fun setupBackNavigationHome(fragment: Fragment, navController: NavController) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 여기에 뒤로가기 버튼을 눌렀을 때 수행할 작업
                    navController.navigate(R.id.findFragment)
                }
            }
            // 콜백을 프래그먼트의 뒤로가기 디스패처에 추가
            fragment.requireActivity().onBackPressedDispatcher.addCallback(fragment, callback)
        }
    //등록하기에서 사용할 뒤로가기 기능
    fun handleBackPressRegister(fragment: Fragment, navController: NavController) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val activity = fragment.activity
                val currentFocusView = activity?.currentFocus

                if (currentFocusView is EditText) {
                    hideKeyboard(activity)
                    currentFocusView.clearFocus()
                } else {
                    AlertDialog.Builder(fragment.requireContext())
                        .setTitle("")
                        .setMessage("등록을 종료하시겠습니까?")
                        .setPositiveButton("확인") { _, _ ->
                            navController.popBackStack()
                        }
                        .setNegativeButton("취소", null)
                        .show()
                }
            }
        }
        fragment.requireActivity().onBackPressedDispatcher.addCallback(fragment, callback)
    }
    //키보드 생성 됐을 때 강종 안 되게하는 코드(기본 뒤로 가기)
    fun handleBackPress(fragment: Fragment, navController: NavController) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val activity = fragment.activity
                val currentFocusView = activity?.currentFocus

                if (currentFocusView is EditText) {
                    hideKeyboard(activity)
                    currentFocusView.clearFocus()
                } else {
                    navController.popBackStack()
                }
            }
        }
        fragment.requireActivity().onBackPressedDispatcher.addCallback(fragment, callback)
    }
    //키보드 숨기는 코드
    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }




}