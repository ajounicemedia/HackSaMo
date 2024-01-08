package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.ChatListInfo
import com.example.god_of_teaching.model.dataclass.ChatMessageInfo
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.example.god_of_teaching.model.repository.StudentChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentChatViewModel@Inject constructor
    (private val studentChatRepository: StudentChatRepository,
     private val userDataStoreHelper : UserDataStoreHelper,
     application: Application
): AndroidViewModel(application) {


    // 사진 업로드 및 메시지 전송
    fun uploadStudentChatRoomPhotosAndSendMessage(otherUid: String, uris: List<Uri>, messageTime:String, studentUid:String, studentNickname:String, fcmKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            val serverKey = userDataStoreHelper.getServerKey.first()
            studentChatRepository.uploadStudentChatRoomPhotosAndCreateMessage(myUid, otherUid, uris, messageTime,myNickname,serverKey, fcmKey)
            studentChatRepository.updateStudentChatList(myUid,studentUid,myNickname,studentNickname,"사진", messageTime)
        }
    }
    //채팅리스트 업데이트
    fun updateStudentChatList(studentUid:String, studentName:String, lastMessage:String, lastMessageTime:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            studentChatRepository.updateStudentChatList(myUid,studentUid,myNickname,studentName,lastMessage, lastMessageTime)
        }
    }
    //새로운 메시지 왔는지 채팅 리스트에서 체크
    fun changeMessageStatus(studentUid:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            studentChatRepository.changeStudentMessageStatus(myUid,studentUid)
        }
    }
    //채팅리스트 받아오기
    var studentChatList: LiveData<List<ChatListInfo>>?=null
    fun getStudentChatList()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            studentChatList = studentChatRepository.getStudentChatList(myUid)
        }
    }



    //채팅방 업데이트
    fun updateStudentChatRoom(otherUid:String, message:String, messageTime:String,fcmKey: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val serverKey = userDataStoreHelper.getServerKey.first()
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            studentChatRepository.updateStudentChatRoom(myUid,otherUid,message,messageTime,myNickname,serverKey,fcmKey)
        }
    }
    var studentChatMessages: LiveData<List<ChatMessageInfo>>?=null//채팅 변경 감지
    //메세지 받아오기
    fun getStudentMessage(otherUid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            studentChatMessages = studentChatRepository.getStudentChatRoom(otherUid, myUid)
        }
    }

    var studentInfo: LiveData<StudentInfo>?=null//채팅방에서 선생 상세보기
    //채팅방에서 학생 정보 불러오기
    fun getStudentInfo(otherUid: String)
    {
        studentInfo = studentChatRepository.getStudentInfo(otherUid)
    }

    fun goOutStudentChatRoom(otherUid: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            studentChatRepository.goOutStudentChatRoom(myUid,otherUid)
        }
    }
}