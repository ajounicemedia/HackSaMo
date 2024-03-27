package god.example.god_of_teaching.model.dataclass

import com.google.firebase.Timestamp

data class ChatMessageInfo (
    val userId: String?=null,
    val message: String?=null,
    val messageTime:String?=null,
    val readCheck:Boolean?=false,
    val timeStamp: Timestamp?=null,
    val messageType: String?="text",
    val photoUri : String?=null
)