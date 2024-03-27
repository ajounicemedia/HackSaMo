package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.dataclass.ChatListInfo
import god.example.god_of_teaching.model.dataclass.ChatMessageInfo
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.repository.TeacherToStudentChatRepository
import javax.inject.Inject

@HiltViewModel
class TeacherToStudentChatViewModel@Inject constructor
    (private val teacherToStudentChatRepository: TeacherToStudentChatRepository,
     application: Application
): AndroidViewModel(application) {
    //teacherToStudentChatRepository - goOutChatRoom 호출
    fun goOutChatRoom(otherUid: String)
    {
        teacherToStudentChatRepository.goOutChatRoom(otherUid)
    }
    //teacherToStudentChatRepository - createChatRoom 호출
    fun createChatRoom(otherUid:String, otherNickname:String, message:String, messageTime:String, otherChatType:String, myChatType:String)
    {
        teacherToStudentChatRepository.createChatRoom(otherUid,otherNickname,message, messageTime,otherChatType,myChatType)
    }
    //teacherToStudentChatRepository - updateChatList 호출
    fun updateChatList(otherUid:String, studentName:String, lastMessage:String, lastMessageTime:String)
    {
        teacherToStudentChatRepository.updateChatList(otherUid,studentName,lastMessage, lastMessageTime)
    }
    //teacherToStudentChatRepository - changeMessageStatus 호출
    fun changeMessageStatus(studentUid:String)
    {
        teacherToStudentChatRepository.changeMessageStatus(studentUid)
    }
    //teacherToStudentChatRepository - getChatList 호출
    var chatList: LiveData<List<ChatListInfo>>?=null
    fun getChatList()
    {
        chatList = teacherToStudentChatRepository.getChatList()
    }
    //teacherToStudentChatRepository - updateChatRoom 호출
    fun updateChatRoom(otherUid:String, message:String, messageTime:String, fcmKey: String)
    {
        teacherToStudentChatRepository.updateChatRoom(otherUid,message,messageTime,fcmKey)
    }
    var studentChatMessages: LiveData<List<ChatMessageInfo>>?=null//채팅 변경 감지
    //teacherToStudentChatRepository - getChatRoom 호출
    fun getMessage(otherUid: String)
    {
        studentChatMessages = teacherToStudentChatRepository.getChatRoom(otherUid)
    }
    //teacherToStudentChatRepository - updatePhotoChatRoom, updateChatList 호출
    fun sendPhotoMessage(otherUid: String, uris: List<Uri>, messageTime:String, studentNickname:String, fcmKey: String) {
            teacherToStudentChatRepository.updatePhotoChatRoom(otherUid, uris, messageTime, fcmKey)
            teacherToStudentChatRepository.updateChatList(otherUid,studentNickname,"사진", messageTime)
    }
    //teacherToStudentChatRepository - getStudentInfo 호출
    var studentInfo: LiveData<StudentInfo>?=null//채팅방에서 선생 상세보기
    fun getStudentInfo(otherUid: String)
    {
        studentInfo = teacherToStudentChatRepository.getStudentInfo(otherUid)
    }

}