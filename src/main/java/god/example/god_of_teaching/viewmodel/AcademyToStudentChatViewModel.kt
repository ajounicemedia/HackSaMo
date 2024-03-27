package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.dataclass.ChatMessageInfo
import god.example.god_of_teaching.model.repository.AcademyToStudentChatRepository
import javax.inject.Inject
@HiltViewModel
class AcademyToStudentChatViewModel@Inject constructor
    (private val academyToStudentChatRepository: AcademyToStudentChatRepository,
     application: Application
): AndroidViewModel(application) {
    //academyToStudentChatRepository - goOutChatRoom 호출
    fun goOutChatRoom(otherUid: String)
    {
        academyToStudentChatRepository.goOutChatRoom(otherUid)
    }
    //academyToStudentChatRepository - createChatRoom 호출
    fun createChatRoom(otherUid:String, otherName:String, message:String, messageTime:String, otherChatType:String, myChatType:String)
    {
        academyToStudentChatRepository.createChatRoom(otherUid,otherName,message, messageTime,otherChatType,myChatType)
    }
    //academyToStudentChatRepository - updateChatList 호출
    fun updateChatList(otherUid:String, otherNickname:String, lastMessage:String, lastMessageTime:String)
    {
        academyToStudentChatRepository.updateChatList(otherUid,otherNickname,lastMessage, lastMessageTime)
    }
    //academyToStudentChatRepository - changeMessageStatus 호출
    fun changeMessageStatus(otherUid:String)
    {
        academyToStudentChatRepository.changeMessageStatus(otherUid)
    }
    //academyToStudentChatRepository - updateChatRoom 호출
    fun updateChatRoom(otherUid:String, message:String, messageTime:String, fcmKey: String)
    {
        academyToStudentChatRepository.updateChatRoom(otherUid,message,messageTime,fcmKey)
    }
    //academyToStudentChatRepository - getChatRoom( 호출
    var chatMessages: LiveData<List<ChatMessageInfo>>?=null
    fun getMessage(otherUid: String)
    {
        chatMessages = academyToStudentChatRepository.getChatRoom(otherUid)
    }
    //academyToStudentChatRepository - updatePhotoChatRoom, updateChatList 호출
    fun sendPhotoMessage(otherUid: String, uris: List<Uri>, messageTime:String, academyNickname:String, fcmKey: String) {
        academyToStudentChatRepository.updatePhotoChatRoom(otherUid, uris, messageTime,fcmKey)
        academyToStudentChatRepository.updateChatList(otherUid,academyNickname,"사진", messageTime)
    }

}