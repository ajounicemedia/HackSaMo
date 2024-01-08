package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.repository.MyInfoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

//내정보레포지토리 호출 + 로컬에 데이터 삽입
@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val userDataStoreHelper : UserDataStoreHelper,
    private val myInfoRepository : MyInfoRepository,
    application: Application) : AndroidViewModel(application) {

    //닉네임변경
    fun changeNicknameLocal(nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertNickname(nickname)
        }
    }
//닉네임변경관찰
    private val _nicknameChangeCheck = MutableLiveData<Boolean>()
    val nicknameChangeCheck: LiveData<Boolean>
        get() = _nicknameChangeCheck

    //닉네임 변경
    fun changeNickname(nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper?.getUid?.collect { uid ->
                myInfoRepository.changeNickname(nickname, uid) {
                    _nicknameChangeCheck.value = it
                    Log.d("uid잘 전달 되나", uid)
                }

            }

        }
    }

    private val _passwordCheck = MutableLiveData<Boolean>()
    val passwordCheck: LiveData<Boolean>
        get() = _passwordCheck

    //비밀번호 관찰
    fun passwordCheck(password: String) {
        myInfoRepository.passwordCheck(password)
        {
            _passwordCheck.value = it
        }
    }

    private val _changePasswordCheck = MutableLiveData<Boolean>()
    val changePasswordCheck: LiveData<Boolean>
        get() = _changePasswordCheck
    //비밀번호 변경
    fun changePassword(password: String) {
        myInfoRepository.changePassword(password)
        {
            _changePasswordCheck.value = it
        }
    }
    //유저 상태 변경
    fun changeMyStatus(status: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            myInfoRepository.changeUserStatus(myUid,status)
            userDataStoreHelper.insertStatus(status)
        }
    }


}