package com.example.god_of_teaching.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel@Inject constructor
    (private val chatRepository: ChatRepository,
     private val userDataStoreHelper : UserDataStoreHelper,
     application: Application
): AndroidViewModel(application) {
    //블랙리스트 추가
    fun addBlackList(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            chatRepository.addBlackList(myUid,otherUid)
        }
    }
    //블랙리스트 삭제
    fun removeBlackList(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            chatRepository.removeBlackList(myUid,otherUid)
        }
    }
    //알람목록추가 ---->캐시삭제 대비해서 파이어베이스에 저장
    //알람차단
    fun addBlackListNotify(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            chatRepository.addBlackListNotify(myUid,otherUid)
        }
    }
    //알람차단해제
    fun removeBlackListNotify(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            chatRepository.removeBlackListNotify(myUid,otherUid)
        }
    }
    //차단 당한 유저 이유 업로드
    fun uploadReportReason(reportedUid : String,reportReason: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            chatRepository.uploadReportReason(myUid,reportedUid,reportReason)
            chatRepository.uploadReportUserUid(myUid,reportedUid)
        }
    }
}