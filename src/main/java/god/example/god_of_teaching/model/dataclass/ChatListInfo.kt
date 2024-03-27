package god.example.god_of_teaching.model.dataclass

import com.google.firebase.Timestamp

data class ChatListInfo(
    val lastMessage: String?=null,
    val lastMessageTime: String?=null,
    val otherName: String?=null,
    val otherUid: String?=null,
    val timeStamp: Timestamp? =null,
    val newMessage: Boolean?= false,
    val myChatType: String?=null
    )