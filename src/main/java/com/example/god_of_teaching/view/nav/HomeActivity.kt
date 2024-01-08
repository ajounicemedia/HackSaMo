package com.example.god_of_teaching.view.nav


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.ActivityHomeBinding
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.viewmodel.AcademyViewModel
import com.example.god_of_teaching.viewmodel.AuthViewModel
import com.example.god_of_teaching.viewmodel.StudentViewModel
import com.example.god_of_teaching.viewmodel.TeacherViewModel
import com.google.auth.oauth2.GoogleCredentials

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@AndroidEntryPoint
//홈 액티비티
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    //미리 데이터 받아오기 위해서
    private val authView: AuthViewModel by viewModels()
    private val studentView : StudentViewModel by viewModels()
    private val teacherView : TeacherViewModel by viewModels()
    private val academyView : AcademyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     //  CoroutineScope(Dispatchers.IO).launch{
                cacheCheck()
                getServerKey()

                binding = ActivityHomeBinding.inflate(layoutInflater)
                val view = binding.root
                setContentView(view)
                basicSetting()
                askNotificationPermission()

       // }

    }
    //기본 세팅
    private fun basicSetting()
    {
        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)
    }
    //유저가 캐시 초기화 했는지 체크
    private fun cacheCheck()
    {
        var currentUid = Firebase.auth.currentUser?.uid
        val userDataStoreHelper = UserDataStoreHelper.getInstance(application)
        CoroutineScope(Dispatchers.IO).launch {

           // var value = userDataStoreHelper.getUid.first()
                //if(value=="default")
                //{
                    authView.getNickNameUidEmailFromFirestore(currentUid !!)

                //}

           // delay(1000)

            val value = userDataStoreHelper.getUid.first()
            val studentCheck = userDataStoreHelper.getStudent.first()
            val teacherCheck= userDataStoreHelper.getTeacher.first()
            val academyCheck= userDataStoreHelper.getAcademy.first()
            Log.d("321321",value)
            Log.d("321321",studentCheck.toString())
            Log.d("321321",teacherCheck.toString())
            Log.d("321321",academyCheck.toString())
            if(studentCheck)
            {
                studentView.getStudentAllData(value!!, this@HomeActivity)
            }
            //캐시 날라갔을 때 선생님 정보 불러오기
            if(teacherCheck)
            {
                teacherView.getTeacherAllData(value!!, this@HomeActivity)
            }
            //캐시 날라갔을 때 학원 정보 불러오기
            if(academyCheck)
            {
                academyView.getTeacherAllData(value!!, this@HomeActivity)
            }

        }
    }


    //알람권한 설정
        // Declare the launcher at the top of your Activity/Fragment:
        private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // TODO: Inform user that that your app will not show notifications.
            }
        }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionRationalDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    //안 받으면 뭐 보일껀지
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationalDialog() {
        AlertDialog.Builder(this)
            .setMessage("알림 권한이 없으면 알림을 받을 수 없습니다.")
            .setPositiveButton("권한 허용하기") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }.setNegativeButton("취소") { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }
    //서버키 로컬에 갱신
    private fun getServerKey()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val userDataStoreHelper = UserDataStoreHelper.getInstance(application)
            userDataStoreHelper.insertServerKey(getAccessToken())
        }
    }
    //서버 키 발급
    private fun getAccessToken(): String {

        val context = this // 혹은 다른 Context
        val assetManager = context.assets
        val keyStream: InputStream = assetManager.open(getString(R.string.json_key))
        val credentials = GoogleCredentials.fromStream(keyStream)
            .createScoped(listOf(getString(R.string.firebase_messaging_key)))
        credentials.refreshIfExpired()
        return credentials.accessToken.tokenValue

    }





}








