package god.example.god_of_teaching.model.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import god.example.god_of_teaching.model.dataclass.ChatMessageInfo
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class AcademyToStudentChatRepository@Inject constructor (private val userDataStoreHelper : UserDataStoreHelper) {
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    //채팅방 나가기
    fun goOutChatRoom(otherUid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            db.collection("studentChatList").document(myUid).collection("other")
                .document(otherUid).delete()
                .addOnFailureListener {
                    Log.d("버그", "AcademyToStudentChatRepository에서 채팅방 못 나가는 중 : ${it.message}")
                }

            db.collection("studentChatRoom").document(myUid)
                .collection(otherUid).document().delete()
                .addOnFailureListener {

                    Log.d("버그", "AcademyToStudentChatRepository에서 채팅방 못 나가는 중 : ${it.message}")
                }
        }
    }
    //채팅방 생성
    fun createChatRoom(
        academyUid: String, academyNickname: String, message: String,
        messageTime: String, academyChatType: String, myChatType : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()

            val academyChatListInfo = hashMapOf(
                "lastMessage" to message,
                "lastMessageTime" to messageTime,
                "otherName" to myNickname,
                "otherUid" to myUid,
                "timeStamp" to FieldValue.serverTimestamp(),
                "newMessage" to true,
                "myChatType" to academyChatType
            )

            val myChatListInfo = hashMapOf(
                "lastMessage" to message,
                "lastMessageTime" to messageTime,
                "otherName" to academyNickname,
                "otherUid" to academyUid,
                "timeStamp" to FieldValue.serverTimestamp(),
                "newMessage" to false,
                "myChatType" to myChatType
            )

            db.collection("academyChatList").document(academyUid).collection("other")
                .document(myUid)
                .set(academyChatListInfo)
                .addOnFailureListener { exception ->
                    Log.d("버그", "AcademyToStudentChatRepository에서 상대 선생님 채팅방 생성 못하는 중 : ${exception.message}")
                }

            db.collection("studentChatList").document(myUid).collection("other")
                .document(academyUid)
                .set(myChatListInfo)
                .addOnFailureListener { exception ->
                    Log.d("버그", "AcademyToStudentChatRepository에서 내 채팅방 생성 못하는 중 : ${exception.message}")
                }
        }
    }
    //채팅방 리스트 업데이트
    fun updateChatList(otherUid: String, otherNickname: String, lastMessage: String, lastMessageTime: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()

            val otherChatListInfo = hashMapOf(
                "lastMessage" to lastMessage,
                "lastMessageTime" to lastMessageTime,
                "otherName" to myNickname,
                "otherUid" to myUid,
                "timeStamp" to FieldValue.serverTimestamp(),
                "newMessage" to true
            )

            val myChatListInfo = hashMapOf(
                "lastMessage" to lastMessage,
                "lastMessageTime" to lastMessageTime,
                "otherName" to otherNickname,
                "otherUid" to otherUid,
                "timeStamp" to FieldValue.serverTimestamp(),
                "newMessage" to false
            )

            db.collection("users").document(otherUid).collection("blacklistUid").document(myUid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        db.collection("studentChatList").document(myUid).collection("other")
                            .document(otherUid)
                            .set(myChatListInfo, SetOptions.merge())
                            .addOnFailureListener { exception ->
                                Log.d("버그", "AcademyToStudentChatRepository에서 내 채팅방 생성 못하는 중 : ${exception.message}")
                            }
                    }
                    else {
                        db.collection("academyChatList").document(otherUid).collection("other")
                            .document(myUid)
                            .set(otherChatListInfo, SetOptions.merge())
                            .addOnFailureListener { exception ->
                                Log.d("버그", "StudentToAcademyChatRepository에서 상대 선생님 채팅방 생성 못하는 중 : ${exception.message}")
                            }

                        db.collection("studentChatList").document(myUid).collection("other")
                            .document(otherUid)
                            .set(myChatListInfo, SetOptions.merge())
                            .addOnFailureListener { exception ->
                                Log.d("버그", "StudentToAcademyChatRepository에서 내 채팅방 생성 못하는 중 : ${exception.message}")
                            }
                    }
                }.addOnFailureListener { exception ->
                    Log.d("버그", "블랙리스트 확인 중 오류 발생: ${exception.message}")
                }
        }
    }
    //채팅방 내에서 채팅 업데이트 됐을 때 내 채팅리스트 newMessage 수정(채팅방 안에서 신규 메시지를 확인 했기 때문)
    fun changeMessageStatus(otherUid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()

            val myChatListInfo = hashMapOf(
                "newMessage" to false
            )
            db.collection("studentChatList").document(myUid).collection("other")
                .document(otherUid)
                .set(myChatListInfo, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "StudentToAcademyChatRepository에서 채팅리스트 상태 설정 변경 실패")
                }
        }
    }


    //채팅방에서 메세지 전송
    fun updateChatRoom(
        otherUid: String, message: String, messageTime: String,fcmKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val serverKey = userDataStoreHelper.getServerKey.first()
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            val messageData = hashMapOf(
                "userId" to myUid,
                "message" to message,
                "messageTime" to messageTime,
                "readCheck" to false,
                "timeStamp" to FieldValue.serverTimestamp(),
                "messageType" to "text",
                "photoUri" to null
            )
            db.collection("users").document(otherUid).collection("blacklistUid").document(myUid)
                .get()
                .addOnSuccessListener { document ->

                    if (document.exists()) {
                        db.collection("studentChatRoom").document(myUid)
                            .collection(otherUid).add(messageData)
                            .addOnSuccessListener {
                                // 메시지 저장 성공 처리
                            }.addOnFailureListener { e ->
                                Log.e("버그", "StudentToAcademyChatRepository - 채팅 전송 못하는 중: ${e.message}")
                            }

                    } else {
                        db.collection("studentChatRoom").document(myUid)
                            .collection(otherUid).add(messageData)
                            .addOnFailureListener { e ->
                                Log.e("버그", "StudentToAcademyChatRepository - 채팅 전송 못하는 중: ${e.message}")
                            }

                        db.collection("academyChatRoom").document(otherUid)
                            .collection(myUid).add(messageData)
                            .addOnSuccessListener {
                                db.collection("users").document(otherUid)
                                    .collection("blacklistNotify").document(myUid).get()
                                    .addOnSuccessListener { it ->
                                        if (!it.exists()) {
                                            sendNotification(otherUid, myNickname, message, serverKey, fcmKey)
                                        }
                                    }.addOnFailureListener {
                                        sendNotification(otherUid, myNickname, message, serverKey, fcmKey)
                                        it.message?.let { it1 -> Log.d("버그", "StudentToAcademyChatRepository - 알림 차단 목록 못 찾는 중 $it1") }
                                    }
                            }.addOnFailureListener { e ->
                                Log.e("버그", "StudentToAcademyChatRepository - Failed to send message: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("버그", "StudentToAcademyChatRepository - 블랙리스트 확인 중 오류 발생: ${exception.message}")
                }
        }
    }
    //채팅방 메시지를 가져오는 메서드
    fun getChatRoom(otherUid: String): LiveData<List<ChatMessageInfo>> {

        val chatMessages = MutableLiveData<List<ChatMessageInfo>>()
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            db.collection("studentChatRoom").document(myUid)
                .collection(otherUid)
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("버그", "StudentToAcademyChatRepository - Listen failed.", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val updatedMessages = chatMessages.value?.toMutableList() ?: mutableListOf()

                        for (docChange in snapshot.documentChanges) {
                            val msg = docChange.document.toObject(ChatMessageInfo::class.java)
                            when (docChange.type) {
                                DocumentChange.Type.ADDED -> updatedMessages.add(msg)
                                DocumentChange.Type.MODIFIED -> {
                                    val index =
                                        updatedMessages.indexOfFirst { it.timeStamp == msg.timeStamp }
                                    if (index != -1) {
                                        updatedMessages[index] = msg
                                    }
                                }
                                // 필요한 경우 삭제된 메시지 처리 (DocumentChange.Type.REMOVED)
                                else -> {}
                            }
                        }
                        chatMessages.value = updatedMessages
                    }
                }
        }
        return chatMessages
    }
    //채팅방에서 사진 전송
    fun updatePhotoChatRoom(otherUid: String, uris: List<Uri>, messageTime: String, fcmKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val myNickname = userDataStoreHelper.getNickname.first()
            val serverKey = userDataStoreHelper.getServerKey.first()
            uris.forEach { uri ->
                val fileName = UUID.randomUUID().toString()
                val photoRef = storage.reference.child("academyChatPhotos/$fileName")

                photoRef.putFile(uri).addOnSuccessListener {
                    photoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        createPhotoMessage(myUid, otherUid, downloadUrl.toString(), messageTime, myNickname, serverKey, fcmKey)
                    }
                }.addOnFailureListener { e ->
                    Log.e("버그", "StudentToAcademyChatRepository - Failed to upload photo: ${e.message}")
                }
            }
        }
    }
    //사진 메시지 생성
    private fun createPhotoMessage(myUid: String, otherUid: String, photoUrl: String, messageTime: String
                                   , myNickname:String, serverKey: String, fcmKey: String) {
        val messageData = hashMapOf(
            "userId" to myUid,
            "message" to "사진",
            "messageTime" to messageTime,
            "readCheck" to false,
            "timeStamp" to FieldValue.serverTimestamp(),
            "messageType" to "photo",
            "photoUri" to photoUrl
        )
        db.collection("users").document(otherUid).collection("blacklist").document(myUid).get()
            .addOnSuccessListener { document ->
                if(document.exists())
                {
                    db.collection("academyChatRoom").document(myUid)
                        .collection(otherUid).add(messageData)
                        .addOnFailureListener { e ->
                            Log.e("버그", "StudentToAcademyChatRepository - 사진 전송 못하는 중: ${e.message}")
                        }
                }
                else
                {
                    db.collection("academyChatRoom").document(myUid)
                        .collection(otherUid).add(messageData)
                        .addOnFailureListener { e ->
                            Log.e("버그", "StudentToAcademyChatRepository - 사진 전송 못하는 중: ${e.message}")
                        }

                    db.collection("academyChatRoom").document(otherUid)
                        .collection(myUid).add(messageData)
                        .addOnSuccessListener {
                            db.collection("users").document(otherUid).collection("blacklistNotify").document(myUid).get()
                                .addOnSuccessListener { it->
                                    if(!it.exists())
                                    {
                                        sendNotification(otherUid,myNickname,"사진",serverKey, fcmKey)
                                    }
                                }.
                                addOnFailureListener {
                                    it.message?.let { it1 -> Log.d("버그", "StudentToAcademyChatRepository - 알림차단리스트 조회 못하는 중$it1") }
                                }
                        }.addOnFailureListener { e ->
                            Log.e("버그", "StudentToAcademyChatRepository - Failed to send message: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "StudentToAcademyChatRepository - 블랙리스트 확인 중 오류 발생: ${exception.message}")
            }
    }
    //알람 보내기
    private fun sendNotification(otherUid: String,myNickname: String,message: String,serverKey: String, fcmKey: String)
    {
        db.collection("users").document(otherUid).get().addOnSuccessListener { document ->
            val otherToken = document.getString("fcmToken") ?: return@addOnSuccessListener

            val jsonMediaType = "application/json; charset=utf-8".toMediaType()
            val jsonObject = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("token", otherToken)
                    put("notification", JSONObject().apply {
                        put("body", message)
                        put("title", myNickname)
                    })

                    put("data", JSONObject().apply {
                        put("body", message)
                        put("title", myNickname)
                    })
                })
            }

            val requestBody = jsonObject.toString().toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(fcmKey)
                .post(requestBody)
                .addHeader("Authorization", "Bearer $serverKey")
                .addHeader("Content-Type", "application/json")
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.d("FCM Response", "Response Code: ${response.code} - ${response.body?.string()}")
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.d("FCM Response", "Response Code: $e")
                }
            })
        }
    }

    //읽음처리
//    fun changeAcademyReadCheck(myUid: String, otherUid: String)
//    {
//        db.collection("academychatroom").document(otherUid)
//            .collection(myUid)
//            .whereEqualTo("readCheck", false) // readCheck가 false인 문서만 선택
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    db.collection("academychatroom").document(otherUid)
//                        .collection(myUid).document(document.id)
//                        .update("readCheck", true) // readCheck를 true로 업데이트
//                    Log.d("321321321321","작동 잘 되고 있음")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("버그", "Error updating documents: ", exception)
//            }
//    }
}