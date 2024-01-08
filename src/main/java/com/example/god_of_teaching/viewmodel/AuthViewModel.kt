package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.repository.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

//인증레포지토리 호출 + 라이브 데이터 관찰
@HiltViewModel
class AuthViewModel @Inject constructor (application: Application,
    private val userDataStoreHelper: UserDataStoreHelper,
    private val authRepository:AuthRepository) : AndroidViewModel(application){


    //로그인 체크
    private val _loginCheck = MutableLiveData<Boolean>()
    val loginCheck: LiveData<Boolean>
    get() = _loginCheck
    //회원가입 체크
    private val _joinCheck = MutableLiveData<Boolean>()
    val joinCheck: LiveData<Boolean>
    get() = _joinCheck
    private val db = Firebase.firestore
    //로그아웃 체크
    private val _logoutCheck = MutableLiveData<Boolean>()
    val logoutCheck: LiveData<Boolean>
        get() = _logoutCheck
    //비밀번호 찾기
    private val _emailCheck = MutableLiveData<Boolean>()
    val  emailCheck: LiveData<Boolean>
        get() = _emailCheck
    //계정 삭제
    private val _accountDeleteCheck = MutableLiveData<Boolean>()
    val  accountDeleteCheck: LiveData<Boolean>
        get() = _accountDeleteCheck



    //로그인
    fun login(email:String,password: String)
    {
        authRepository.login(email,password)
        {
            _loginCheck.value = it
        }
    }
    //회원가입
    fun join(email: String,password: String,nickname: String)
    {
        authRepository.join(email,password, nickname)
        {
            _joinCheck.value = it
        }
    }

    //캐시없을 떄 파이어베이스에서 UID로컬에서 가져오기
     fun getNickNameUidEmailFromFirestore(uid: String)
    {

        //val uid = Firebase.auth.currentUser?.uid
        val userRef = uid?.let { db.collection("users").document(it).get() }

        userRef?.addOnSuccessListener { document ->
            if (document != null) {

                val nickname = document.getString("nickname")
                val email = document.getString("email")
                val teacher = document.getBoolean("teacher")
                val academy = document.getBoolean("academy")
                val student = document.getBoolean("student")
                val status = document.getString("status")
                CoroutineScope(Dispatchers.IO).launch {
                    userDataStoreHelper.insertUid(uid)
                    if (nickname != null) {
                        userDataStoreHelper.insertNickname(nickname)
                    }
                    if (email != null) {
                        userDataStoreHelper.insertEmail(email)
                    }
                    if (status != null) {
                        userDataStoreHelper.insertStatus(status)
                    }
                    if (academy != null) {
                        userDataStoreHelper.insertStudent(academy)
                    }
                    if (teacher != null) {
                        userDataStoreHelper.insertTeacher(teacher)
                    }

                    if (student != null) {
                        userDataStoreHelper.insertAcademy(student)
                    }



                }
            } else {
                Log.d("버그", "인증뷰모델에서 uid 없음")
            }}
            ?.addOnFailureListener { exception ->
                Log.d("버그", "인뷰모델에서 uid 못 찾고 있음", exception)
            }


    }
    fun getBlackListBlockNotification(uid: String, sharedPreferences: SharedPreferences) {
        db.collection("users").document(uid).collection("blacklist")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val blackList = querySnapshot.documents.mapNotNull { it.getString("blacklist") }.toSet() // List를 Set으로 변환
                val editor = sharedPreferences.edit()
                editor.putStringSet("blacklist", blackList)
                editor.apply()
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "블랙리스트 조회 중 오류: ${exception.message}")
            }

        db.collection("users").document(uid).collection("blacklistNotify")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val blackNotificationList = querySnapshot.documents.mapNotNull { it.getString("blacklistNotify") }.toSet() // List를 Set으로 변환
                val editor = sharedPreferences.edit()
                editor.putStringSet("blockNotification", blackNotificationList)
                editor.apply()
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "블랙리스트 조회 중 오류: ${exception.message}")
            }
        db.collection("users").document(uid).collection("reportedUid")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val reportList = querySnapshot.documents.mapNotNull { it.getString("reportedUid") }.toSet() // List를 Set으로 변환
                val editor = sharedPreferences.edit()
                editor.putStringSet("reportList", reportList)
                editor.apply()
            }
            .addOnFailureListener { exception ->
                Log.d("Error", "블랙리스트 조회 중 오류: ${exception.message}")
            }

    }

    //로컬에 데이터 업로드
    fun insertNicknameEmailLocal(nickname: String,email: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertNickname(nickname)
            userDataStoreHelper.insertNickname(email)
        }
    }

    //로컬에 데이터 업로드
    fun insertUidLocal(uid:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertUid(uid)
        }
    }
    //로그아웃
    fun logout()
    {
        authRepository.logout() {
            _logoutCheck.value=it
        }
    }
    //비밀번호 찾기
    fun sendEmail(email: String)
    {
        authRepository.findPassword(email)
        {
            _emailCheck.value=it
        }
    }
    //계정 삭제
    fun deleteAccount()
    {
        authRepository.deleteAccount {
            _accountDeleteCheck.value = it
        }
    }



    val readNickname = userDataStoreHelper.getNickname.asLiveData()
    val readEmail = userDataStoreHelper.getEmail.asLiveData()


}