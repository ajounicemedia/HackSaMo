package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.dataclass.ChatListInfo
import god.example.god_of_teaching.model.dataclass.ChatMessageInfo
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.repository.StudentToTeacherChatRepository
import javax.inject.Inject

@HiltViewModel
class StudentToTeacherChatViewModel@Inject constructor
    (private val studentToTeacherChatRepository: StudentToTeacherChatRepository,
     application: Application
): AndroidViewModel(application){

    //studentToTeacherChatRepository - goOutChatRoom 호출
    fun goOutChatRoom(otherUid: String)
    {
        studentToTeacherChatRepository.goOutChatRoom(otherUid)
    }
    //studentToTeacherChatRepository - createChatRoom 호출
    fun createChatRoom(otherUid:String, otherName:String, message:String, messageTime:String, otherChatType:String, myChatType:String)
    {
        studentToTeacherChatRepository.createChatRoom(otherUid,otherName,message, messageTime,otherChatType,myChatType)
    }
    //studentToTeacherChatRepository - updateChatList 호출
    fun updateChatList(otherUid:String, otherName:String, lastMessage:String, lastMessageTime:String)
    {
        studentToTeacherChatRepository.updateChatList(otherUid,otherName,lastMessage,lastMessageTime)
    }
    //studentToTeacherChatRepository - changeMessageStatus 호출
    fun changeMessageStatus(teacherUid:String)
    {
        studentToTeacherChatRepository.changeMessageStatus(teacherUid)
    }
    //studentToTeacherChatRepository - getChatList 호출
    var chatList: LiveData<List<ChatListInfo>?>?=null
    fun getTeacherChatList()
    {
        chatList = studentToTeacherChatRepository.getChatList()
    }
    //studentToTeacherChatRepository - updateChatRoom 호출
    fun updateChatRoom(otherUid:String, message:String, messageTime:String, fcmKey: String)
    {
        studentToTeacherChatRepository.updateChatRoom(otherUid,message,messageTime,fcmKey)
    }
    //studentToTeacherChatRepository - getChatRoom 호출
    var teacherChatMessages: LiveData<List<ChatMessageInfo>>?=null//채팅 변경 감지
    fun getMessage(otherUid: String)
    {
        teacherChatMessages = studentToTeacherChatRepository.getChatRoom(otherUid)
    }
    //studentToTeacherChatRepository - updatePhotoChatRoom, updateChatList 호출
    fun sendPhotoMessage(otherUid: String, uris: List<Uri>, messageTime:String, otherNickname:String, fcmKey: String)
    {
            studentToTeacherChatRepository.updatePhotoChatRoom(otherUid, uris, messageTime, fcmKey)
            studentToTeacherChatRepository.updateChatList(otherUid,otherNickname,"사진", messageTime)
    }
    //studentToTeacherChatRepository - getTeacherInfo 호출
    var teacherInfo: LiveData<TeacherInfo>?=null//채팅방에서 선생 상세보기
    fun getTeacherInfo(otherUid: String)
    {
        teacherInfo = studentToTeacherChatRepository.getTeacherInfo(otherUid)
    }

//페이징
//    private val db = FirebaseFirestore.getInstance()
//    private var teacherChatPagingSource: TeacherChatPagingSource? = null
//    var teacherChatMessages = MutableLiveData<List<ChatMessageInfo>>()
//
//    fun getTeacherMessage(otherUid: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val myUid = userDataStoreHelper.getUid.first()
//            teacherChatPagingSource = TeacherChatPagingSource(db, myUid, otherUid)
//
//            teacherChatPagingSource?.loadInitialMessages { messages ->
//                teacherChatMessages.postValue(messages)
//            }
//        }
//    }
//
//    fun loadPreviousMessages() {
//        teacherChatPagingSource?.loadPreviousMessages { messages ->
//            val currentMessages = teacherChatMessages.value.orEmpty()
//            teacherChatMessages.postValue(currentMessages + messages)
//        }
//    }







    //읽음처리
//   fun changeTeacherReadCheck(otherUid: String)
//    {
//        CoroutineScope(Dispatchers.IO).launch {
//            val myUid = userDataStoreHelper.getUid.first()
//            //chatRepository.changeTeacherReadCheck(myUid,otherUid)
//        }
//    }

}