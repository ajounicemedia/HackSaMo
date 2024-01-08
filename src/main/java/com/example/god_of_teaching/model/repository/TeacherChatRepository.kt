package com.example.god_of_teaching.model.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.god_of_teaching.model.dataclass.ChatListInfo
import com.example.god_of_teaching.model.dataclass.ChatMessageInfo
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
//선생님 채팅 관련 레포지토리
class TeacherChatRepository {
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    //채팅방 나가기
    fun goOutTeacherChatRoom(myUid: String, otherUid: String) {
        db.collection("teacherchatlist").document(myUid).collection("other")
            .document(otherUid).delete()
            .addOnSuccessListener {
                // 성공적으로 삭제되었을 때 로직
            }
            .addOnFailureListener {
                // 삭제 실패 시 로직
                Log.d("버그", "TeacherChatRepository에서 채팅방 못 나가는 중 : ${it.message}")
            }

        db.collection("teacherchatroom").document(myUid)
            .collection(otherUid).document().delete()
            .addOnSuccessListener {
                // 성공적으로 삭제되었을 때 로직
            }
            .addOnFailureListener {
                // 삭제 실패 시 로직
                Log.d("버그", "TeacherChatRepository에서 채팅방 못 나가는 중 : ${it.message}")
            }
    }

    //채팅방 리스트 업데이트
    fun updateTeacherChatList(
        myUid: String,
        otherUid: String,
        myName: String,
        otherName: String,
        lastMessage: String,
        lastMessageTime: String
    ) {
        //대화신청을 받는 선생님 채팅리스트 정보
        val teacherChatListInfo = hashMapOf(
            "lastMessage" to lastMessage,
            "lastMessageTime" to lastMessageTime,
            "otherName" to myName,
            "otherUid" to myUid,
            "timeStamp" to FieldValue.serverTimestamp(),// Firestore 정렬용 타임스탬프
            "newMessage" to true
        )
        //내 채팅리스트 정보
        val myChatListInfo = hashMapOf(
            "lastMessage" to lastMessage,
            "lastMessageTime" to lastMessageTime,
            "otherName" to otherName,
            "otherUid" to otherUid,
            "timeStamp" to FieldValue.serverTimestamp(),// Firestore 정렬용 타임스탬프
            "newMessage" to false
        )

                if (checkBlackList(myUid,otherUid) == true)  //차단 당했으면 자신의 채팅만 업데이트함
                {
                    //내 채팅 리스트 업데이트
                    db.collection("teacherchatlist").document(myUid).collection("other")
                        .document(otherUid)
                        .set(myChatListInfo)
                        .addOnSuccessListener {

                        }.addOnFailureListener { exception ->
                            Log.d(
                                "버그",
                                "TeacherChatRepository에서 내 채팅방 생성 못하는 중 : ${exception.message}"
                            )
                        }

                }
                if (checkBlackList(myUid,otherUid) == false)//블랙리스트 아니면 둘다 업데이트
                {
                    //대화 신청 받는 선생님 채팅 리스트 업데이트
                    db.collection("teacherchatlist").document(otherUid).collection("other")
                        .document(myUid)
                        .set(teacherChatListInfo)
                        .addOnSuccessListener {
                            // 성공 로직
                        }
                        .addOnFailureListener { exception ->
                            Log.d(
                                "버그",
                                "TeacherChatRepository에서 상대 선생님 채팅방 생성 못하는 중 : ${exception.message}"
                            )
                        }


                    //내 채팅 리스트 업데이트
                    db.collection("teacherchatlist").document(myUid).collection("other")
                        .document(otherUid)
                        .set(myChatListInfo)
                        .addOnSuccessListener {

                        }.addOnFailureListener { exception ->
                            Log.d(
                                "버그",
                                "TeacherChatRepository에서 내 채팅방 생성 못하는 중 : ${exception.message}"
                            )
                        }
                }
    }
    //새로운 메시지인지 확인
    fun changeTeacherMessageStatus(myUid: String, otherUid: String) {
        //내 채팅리스트 정보
        val myChatListInfo = hashMapOf(
            "newMessage" to false
        )
        //대화 신청 받는 선생님 채팅 리스트 업데이트
        db.collection("teacherchatlist").document(myUid).collection("other")
            .document(otherUid)
            .set(myChatListInfo, SetOptions.merge())
            .addOnSuccessListener {
                // 성공 로직
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "TeacherChatRepository에서 채팅방 설정 못하는 중")
            }
    }

    //선생님 채팅방 목록 가져오기
    fun getTeacherChatList(myUid: String): LiveData<List<ChatListInfo>> {
        val chatListInfoLiveData = MutableLiveData<List<ChatListInfo>>()

        // 'timestamp' 필드를 기준으로 오름차순 정렬
        db.collection("teacherchatlist").document(myUid)
            .collection("other")
            .orderBy("timeStamp", Query.Direction.ASCENDING) // 또는 Query.Direction.DESCENDING
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ChatRepository", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val chatListInfos = snapshots.toObjects(ChatListInfo::class.java)
                    chatListInfoLiveData.value = chatListInfos
                } else {
                    Log.d("ChatRepository", "Current data: null")
                }
            }

        return chatListInfoLiveData

    }

    //메세지 전송
    fun updateTeacherChatRoom(
        myUid: String,
        otherUid: String,
        message: String,
        messageTime: String,
        myNickname: String,
        serverKey: String,
        fcmKey: String
    ) {
        val messageData = hashMapOf(
            "userId" to myUid,
            "message" to message,
            "messageTime" to messageTime,
            "readCheck" to false,
            "timeStamp" to FieldValue.serverTimestamp(),// Firestore 정렬용 타임스탬프
            "messageType" to "text",
            "photoUri" to null
        )

        if (checkBlackList(myUid, otherUid) == true) //내가 블랙리스트면 내것만 업데이트
        {
            //내 채팅 목록 업데이트
            db.collection("teacherchatroom").document(myUid)
                .collection(otherUid).add(messageData)
                .addOnSuccessListener {}
                .addOnFailureListener { e ->
                    Log.d("버그", "TeacherChatRepository에서 상대방 파이어스토어에 데이터 업로드 못하는 중: ", e)
                }

        }
        if (checkBlackList(myUid, otherUid) == false) //블랙리스트 아니면 둘다 업데이트
            {
            //내 채팅 목록 업데이트
            db.collection("teacherchatroom").document(myUid)
                .collection(otherUid).add(messageData)
                .addOnSuccessListener {}
                .addOnFailureListener { e ->
                    Log.d("버그", "TeacherChatRepository에서 상대방 파이어스토어에 데이터 업로드 못하는 중: ", e)
                }
            //상대방 채팅 목록 업데이트
            db.collection("teacherchatroom").document(otherUid)
                .collection(myUid).add(messageData)
                .addOnSuccessListener {//알림 보내기
                    sendNotification(otherUid, myNickname, message, serverKey, fcmKey)}
                .addOnFailureListener { e ->
                    Log.d("버그", "TeacherChatRepository에서 상대방 파이어스토어에 업로드 못하는 중: ", e)
                }

        }
    }
    // 채팅 메시지를 가져오는 메서드
    fun getTeacherChatRoom(otherUid: String, myUid: String): LiveData<List<ChatMessageInfo>> {

        val chatMessages = MutableLiveData<List<ChatMessageInfo>>()


        db.collection("teacherchatroom").document(myUid)
            .collection(otherUid)
            .orderBy("timeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("버그", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val updatedMessages = chatMessages.value?.toMutableList() ?: mutableListOf()

                    for (docChange in snapshot.documentChanges) {
                        val msg = docChange.document.toObject(ChatMessageInfo::class.java)
                        when (docChange.type) {
                            DocumentChange.Type.ADDED -> updatedMessages.add(msg)
                            DocumentChange.Type.MODIFIED -> {
                                val index = updatedMessages.indexOfFirst { it.timeStamp == msg.timeStamp }
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

        return chatMessages
    }
    //파이어 스토리지에 사진 업로드
    fun uploadTeacherChatRoomPhotosAndCreateMessage(myUid: String, otherUid: String, uris: List<Uri>, messageTime: String,myNickname:String,serverKey: String, fcmKey: String) {
        uris.forEach { uri ->
            val fileName = UUID.randomUUID().toString()
            val photoRef = storage.reference.child("teacher_chat_photos/$fileName")

            photoRef.putFile(uri).addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener  { downloadUrl ->
                    createTeacherSinglePhotoMessage(myUid, otherUid, downloadUrl.toString(), messageTime,myNickname,serverKey,fcmKey)
                }
            }.addOnFailureListener { e ->
                Log.e("버그", "Failed to upload photo: ${e.message}")
            }
        }
    }

    private fun createTeacherSinglePhotoMessage(myUid: String, otherUid: String, photoUrl: String, messageTime: String
    ,myNickname:String,serverKey: String, fcmKey: String) {
        val messageData = hashMapOf(
            "userId" to myUid,
            "message" to "사진",
            "messageTime" to messageTime,
            "readCheck" to false,
            "timeStamp" to FieldValue.serverTimestamp(),
            "messageType" to "photo",
            "photoUri" to photoUrl // 단일 사진 URL
        )

                if (checkBlackList(myUid,otherUid) == true) //내가 블랙리스트면 내것만 업데이트
                {
                    db.collection("teacherchatroom").document(myUid)
                        .collection(otherUid).add(messageData)
                        .addOnSuccessListener {
                            // 메시지 저장 성공 처리
                        }.addOnFailureListener { e ->
                            Log.e("버그", "사진 전송 못하는 중: ${e.message}")
                        }

                }
                if (checkBlackList(myUid,otherUid) == false)//블랙리스트 아니면 둘다 업데이트
                {
                    db.collection("teacherchatroom").document(myUid)
                        .collection(otherUid).add(messageData)
                        .addOnSuccessListener {
                            // 메시지 저장 성공 처리
                        }.addOnFailureListener { e ->
                            Log.e("버그", "사진 전송 못하는 중: ${e.message}")
                        }

                    db.collection("teacherchatroom").document(otherUid)
                        .collection(myUid).add(messageData)
                        .addOnSuccessListener {
                            // 메시지 저장 성공 처리
                            //알림 보내기
                            sendNotification(otherUid,myNickname,"사진",serverKey, fcmKey)
                        }.addOnFailureListener { e ->
                            Log.e("ChatRepository", "Failed to send message: ${e.message}")
                        }


                }
    }
    //선생님 정보 가져오기
    fun getTeacherInfo(teacherUid : String) : LiveData<TeacherInfo>
    {
        val teacherInfo = MutableLiveData<TeacherInfo?>()
        db.collection("teachers").document(teacherUid).get().addOnSuccessListener { documentSnapshot ->
            val info = documentSnapshot.toObject(TeacherInfo::class.java)
            if (info != null) { // null 체크 추가
                teacherInfo.value = info
            }

        }.addOnFailureListener {
            teacherInfo.value = null
            Log.e("버그", "선생님 채팅 레포지토리에서 선생님 정보 못 받아오는 중")
        }
        return teacherInfo as LiveData<TeacherInfo>


    }
    //알람 보내기
    private fun sendNotification(otherUid: String,myNickname: String,message: String,serverKey: String, fcmKey: String)
    {
        //알림 보내기
        db.collection("users").document(otherUid).get().addOnSuccessListener { document ->
            val otherToken = document.getString("fcmToken") ?: return@addOnSuccessListener


            // 여기에서 FCM 메시지 보내기 코드를 실행
            val jsonMediaType = "application/json; charset=utf-8".toMediaType()
            val jsonObject = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("token", otherToken)
                    put("notification", JSONObject().apply {
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
                    // 성공 처리
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.d("FCM Response", "Response Code: $e")
                    // 실패 처리
                }
            })
        }
    }
    //내가 블랙리스트인지 확인
    private fun checkBlackList(myUid: String,otherUid:String) : Boolean?
    {
        var checkBlackList : Boolean?=false
        // 블랙리스트 확인
        db.collection("users").document(myUid).collection("blacklist").document(otherUid).get()
            .addOnSuccessListener { document ->
                checkBlackList = document.exists()
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "블랙리스트 확인 중 오류 발생: ${exception.message}")
            }
        return checkBlackList
    }

    //읽음처리
//    fun changeTeacherReadCheck(myUid: String, otherUid: String)
//    {
//        db.collection("teacherchatroom").document(otherUid)
//            .collection(myUid)
//            .whereEqualTo("readCheck", false) // readCheck가 false인 문서만 선택
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    db.collection("teacherchatroom").document(otherUid)
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