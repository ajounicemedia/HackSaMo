package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.dataclass.AcademyInfo
import god.example.god_of_teaching.model.dataclass.ChatListInfo
import god.example.god_of_teaching.model.dataclass.ChatMessageInfo
import god.example.god_of_teaching.model.repository.StudentToAcademyChatRepository
import javax.inject.Inject
//학원 채팅 뷰모델
@HiltViewModel
class StudentToAcademyChatViewModel@Inject constructor
    (private val studentToAcademyChatRepository: StudentToAcademyChatRepository,
     application: Application
): AndroidViewModel(application){
    //studentToAcademyChatRepository - goOutChatRoom 호출
    fun goOutChatRoom(otherUid: String)
    {
        studentToAcademyChatRepository.goOutChatRoom(otherUid)
    }
    //studentToAcademyChatRepository - createChatRoom 호출
    fun createChatRoom(academyUid:String, academyName:String, message:String, messageTime:String, academyChatType:String, myChatType:String)
    {
        studentToAcademyChatRepository.createChatRoom(academyUid,academyName,message,messageTime,academyChatType,myChatType)
    }
    //studentToAcademyChatRepository - updateChatList 호출
    fun updateChatList(otherUid:String, otherNickname:String, lastMessage:String, lastMessageTime:String)
    {
        studentToAcademyChatRepository.updateChatList(otherUid,otherNickname,lastMessage, lastMessageTime)
    }
    //studentToAcademyChatRepository - changeMessageStatus 호출
    fun changeMessageStatus(otherUid:String)
    {
        studentToAcademyChatRepository.changeMessageStatus(otherUid)
    }
    //studentToAcademyChatRepository - getChatList 호출
    var chatList: LiveData<List<ChatListInfo>>?=null
    fun getChatList()
    {
        chatList = studentToAcademyChatRepository.getChatList()
    }
    //studentToAcademyChatRepository - updateChatRoom 호출
    fun updateChatRoom(otherUid:String, message:String, messageTime:String, fcmKey: String)
    {
        studentToAcademyChatRepository.updateChatRoom(otherUid,message,messageTime,fcmKey)
    }
    //studentToAcademyChatRepository - getChatRoom 호출
    var chatMessages: LiveData<List<ChatMessageInfo>>?=null
    fun getMessage(otherUid: String)
    {
        chatMessages = studentToAcademyChatRepository.getChatRoom(otherUid)
    }
    //studentToAcademyChatRepository - updatePhotoChatRoom, updateChatList 호출
    fun sendPhotoMessage(otherUid: String, uris: List<Uri>, messageTime:String, academyNickname:String, fcmKey: String) {
            studentToAcademyChatRepository.updatePhotoChatRoom(otherUid, uris, messageTime,fcmKey)
            studentToAcademyChatRepository.updateChatList(otherUid,academyNickname,"사진", messageTime)
    }
    //studentToAcademyChatRepository - getAcademyInfo 호출
    var academyInfo: LiveData<AcademyInfo>?=null
    fun getAcademyInfo(otherUid: String)
    {
        academyInfo = studentToAcademyChatRepository.getAcademyInfo(otherUid)
    }
} 
