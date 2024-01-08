package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.ChatListInfo
import com.example.god_of_teaching.model.dataclass.ChatMessageInfo
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.example.god_of_teaching.model.repository.AcademyChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
//학원 채팅 뷰모델
@HiltViewModel
class AcademyChatViewModel@Inject constructor
    (private val academyChatRepository: AcademyChatRepository,
     private val userDataStoreHelper : UserDataStoreHelper,
     application: Application
): AndroidViewModel(application){


    // 사진 업로드 및 메시지 전송
    fun uploadAcademyChatRoomPhotosAndSendMessage(otherUid: String, uris: List<Uri>, messageTime:String, academyUid:String, academyNickname:String, fcmKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            val serverKey = userDataStoreHelper.getServerKey.first()
            academyChatRepository.uploadAcademyChatRoomPhotosAndCreateMessage(myUid, otherUid, uris, messageTime,myNickname,serverKey, fcmKey)
            academyChatRepository.updateAcademyChatList(myUid,academyUid,myNickname,academyNickname,"사진", messageTime)
        }
    }
    //채팅리스트 업데이트
    fun updateAcademyChatList(academyUid:String, academyName:String, lastMessage:String, lastMessageTime:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            academyChatRepository.updateAcademyChatList(myUid,academyUid,myNickname,academyName,lastMessage, lastMessageTime)
        }
    }
    //새로운 메시지 왔는지 채팅 리스트에서 체크
    fun changeMessageStatus(academyUid:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            academyChatRepository.changeAcademyMessageStatus(myUid,academyUid)
        }
    }
    //채팅리스트 받아오기
    var academyChatList: LiveData<List<ChatListInfo>>?=null
    fun getAcademyChatList()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            academyChatList = academyChatRepository.getAcademyChatList(myUid)
        }
    }



    //채팅방 업데이트
    fun updateAcademyChatRoom(otherUid:String, message:String, messageTime:String,fcmKey: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val serverKey = userDataStoreHelper.getServerKey.first()
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            academyChatRepository.updateAcademyChatRoom(myUid,otherUid,message,messageTime,myNickname,serverKey,fcmKey)
        }
    }
    var academyChatMessages: LiveData<List<ChatMessageInfo>>?=null//채팅 변경 감지
    //메세지 받아오기
    fun getAcademyMessage(otherUid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            academyChatMessages = academyChatRepository.getAcademyChatRoom(otherUid, myUid)
        }
    }

    var academyInfo: LiveData<AcademyInfo>?=null//채팅방에서 선생 상세보기
    //채팅방에서 선생님 정보 불러오기
    fun getAcademyInfo(otherUid: String)
    {
        academyInfo = academyChatRepository.getAcademyInfo(otherUid)
    }

    fun goOutAcademyChatRoom(otherUid: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            academyChatRepository.goOutAcademyChatRoom(myUid, otherUid)
        }
    }
} 
