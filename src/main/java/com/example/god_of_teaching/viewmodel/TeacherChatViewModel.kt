package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.ChatListInfo
import com.example.god_of_teaching.model.dataclass.ChatMessageInfo
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.example.god_of_teaching.model.repository.TeacherChatRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherChatViewModel@Inject constructor
    (private val teacherChatRepository: TeacherChatRepository,
     private val userDataStoreHelper : UserDataStoreHelper,
     application: Application
): AndroidViewModel(application){


    // 사진 업로드 및 메시지 전송
    fun uploadTeacherChatRoomPhotosAndSendMessage(otherUid: String, uris: List<Uri>, messageTime:String, teacherUid:String, teacherNickname:String, fcmKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            val serverKey = userDataStoreHelper.getServerKey.first()
            teacherChatRepository.uploadTeacherChatRoomPhotosAndCreateMessage(myUid, otherUid, uris, messageTime,myNickname,serverKey, fcmKey)
            teacherChatRepository.updateTeacherChatList(myUid,teacherUid,myNickname,teacherNickname,"사진", messageTime)
        }
    }
    //채팅리스트 업데이트
    fun updateTeacherChatList(teacherUid:String, teacherName:String, lastMessage:String, lastMessageTime:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            teacherChatRepository.updateTeacherChatList(myUid,teacherUid,myNickname,teacherName,lastMessage, lastMessageTime)
        }
    }
    //새로운 메시지 왔는지 채팅 리스트에서 체크
    fun changeMessageStatus(teacherUid:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            teacherChatRepository.changeTeacherMessageStatus(myUid,teacherUid)
        }
    }
    //채팅리스트 받아오기
    var teacherChatList: LiveData<List<ChatListInfo>>?=null
    fun getTeacherChatList()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            teacherChatList = teacherChatRepository.getTeacherChatList(myUid)
        }
    }



    //채팅방 업데이트
    fun updateTeacherChatRoom(otherUid:String, message:String, messageTime:String,fcmKey: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val serverKey = userDataStoreHelper.getServerKey.first()
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            teacherChatRepository.updateTeacherChatRoom(myUid,otherUid,message,messageTime,myNickname,serverKey,fcmKey)
        }
    }
    var teacherChatMessages: LiveData<List<ChatMessageInfo>>?=null//채팅 변경 감지
    //메세지 받아오기
    fun getTeacherMessage(otherUid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            teacherChatMessages = teacherChatRepository.getTeacherChatRoom(otherUid, myUid)
        }
    }

    var teacherInfo: LiveData<TeacherInfo>?=null//채팅방에서 선생 상세보기
    //채팅방에서 선생님 정보 불러오기
    fun getTeacherInfo(otherUid: String)
    {
        teacherInfo = teacherChatRepository.getTeacherInfo(otherUid)
    }
    //채팅방 나가기
    fun goOutTeacherChatRoom(otherUid: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            teacherChatRepository.goOutTeacherChatRoom(myUid, otherUid)
        }
    }




    //읽음처리
//   fun changeTeacherReadCheck(otherUid: String)
//    {
//        CoroutineScope(Dispatchers.IO).launch {
//            val myUid = userDataStoreHelper.getUid.first()
//            //chatRepository.changeTeacherReadCheck(myUid,otherUid)
//        }
//    }

}