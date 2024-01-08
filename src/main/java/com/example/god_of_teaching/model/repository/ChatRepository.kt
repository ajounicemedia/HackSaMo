package com.example.god_of_teaching.model.repository

import android.util.Log
import com.example.god_of_teaching.model.dataclass.ReportReasonInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//파이어베이스 권한 추가 필요
class ChatRepository() {
    private val db = Firebase.firestore


    //블랙리스트 추가
    fun addBlackList(uid: String,otherUid : String)
    {
        val blacklistData = hashMapOf("blacklist" to otherUid)

        //블랙리스트 추가
        db.collection("users").document(uid).collection("blacklist").add(blacklistData)
            .addOnSuccessListener {
                Log.d("", "ChatRepository - 블랙리스트 추가 성공")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "ChatRepository - 블랙리스트 추가 못하는 중 : ${exception.message} + $uid")
            }
    }
    //블랙리스트 삭제
    fun removeBlackList(uid: String,otherUid : String)
    {


        //블랙리스트 삭제
        db.collection("users").document(uid).collection("blacklist").document(otherUid)
            .delete()
            .addOnSuccessListener {
                Log.d("", "ChatRepository - 블랙리스트 삭제 성공")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "ChatRepository - 블랙리스트 삭제 못하는 중 : ${exception.message} + $uid")
            }
    }
    //알람차단목록추가 ---->캐시삭제 대비해서 파이어베이스에 저장
    fun addBlackListNotify(uid: String,otherUid : String)
    {
        val blacklistData = hashMapOf("blacklistNotify" to otherUid)

        //블랙리스트 추가
        db.collection("users").document(uid).collection("blacklistNotify").add(blacklistData)
            .addOnSuccessListener {
                Log.d("", "ChatRepository - 알람 차단 목록 추가 성공")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "ChatRepository - 알람 차단 목록 추가 못하는 중 : ${exception.message} + $uid")
            }
    }
    //알람차단목록 삭제
    fun removeBlackListNotify(uid: String,otherUid : String)
    {


        //블랙리스트 삭제
        db.collection("users").document(uid).collection("blacklistNotify").document(otherUid)
            .delete()
            .addOnSuccessListener {
                Log.d("", "ChatRepository - 알람 차단 목록 삭제 성공")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "ChatRepository - 알람 차단 목록 삭제 못하는 중 : ${exception.message} + $uid")
            }
    }

    //차단 유저 이유 업로드
    fun uploadReportReason(uid: String,reportedUid : String,reportReason: String)
    {
        val reportData = ReportReasonInfo(uid,reportedUid ,reportReason)
        db.collection("reports").document(reportedUid).collection(uid).add(reportData).addOnSuccessListener {}
            .addOnFailureListener { e ->
                Log.d("버그", "ChatRepository에서 차단 이유 업로드 못하는 중: ", e)
            }
    }
    //신고 유저 uid 업로드 (캐시 삭제 대비해서)
    fun uploadReportUserUid(uid: String,reportedUid : String)
    {
        val reportUserUid = hashMapOf("reportedUid" to reportedUid)
        //신고 유저 uid 추가
        db.collection("users").document(uid).collection("reportedUid").add(reportUserUid)
            .addOnSuccessListener {
                Log.d("", "ChatRepository - 신고 목록 추가 성공")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "ChatRepository - 신고 목록 추가 못하는 중 : ${exception.message} + $uid")
            }
    }
}