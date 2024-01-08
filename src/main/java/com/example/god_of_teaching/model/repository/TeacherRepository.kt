package com.example.god_of_teaching.model.repository

import android.net.Uri
import android.util.Log
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

//선생님 데이터 파이어베이스에 올리기위한 레포지토리
class TeacherRepository {
    private val db = Firebase.firestore

    //선생님 uid, 닉네임, 한 줄 소개, 출생년도, 성별, 학교, 학교 위치, 전공, 재학상태, 수업방식 파이어베이스에 업로드 하는 코드
    fun uploadTeacherInfo(
        uid: String, nickname: String,
        des: String, bornYear: String,
        gender: String, campus: String, campusLocal: String,
        major: String, status: String, way: String
    ) {
        Log.d("32132122",uid)
        val teacherInfo = TeacherInfo(
            uid,
            nickname,
            des,
            bornYear,
            gender,
            campus,
            campusLocal,
            major,
            status,
            way
        )

        db.collection("teachers").document(uid).set(teacherInfo).addOnSuccessListener {
        }.addOnFailureListener { exception ->
            Log.d("버그", "TeacherRepository에서 선생님 정보(가장 많은 곳) 업로드실패: ${exception.message}")
        }

    }
//선생님 지역 파이어베이스에 업로드
    fun uploadTeacherLocal(localList: List<String>, uid:String)
    {
        val data = hashMapOf(
            "availableLocal" to localList
        )
        db.collection("teachers").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "TeacherRepository에서 지역업로드실패: ${exception.message}")
        }
    }
    //선생님 과목 파이어베이스에 업로드
    fun uploadTeacherSubject(subjectList: List<String>, uid:String)
    {
        val data = hashMapOf(
            "subject" to subjectList
        )
        db.collection("teachers").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "TeacherRepository에서과목업로드실패: ${exception.message}")
        }
    }
    //선생님 소개 파이어베이스에 업로드
    fun uploadTeacherIntroduce(introduce: String, uid:String)
    {
        val data = hashMapOf(
            "introduce" to introduce
        )
        db.collection("teachers").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
            Log.d("성공실패", "성공")
        }.addOnFailureListener {exception ->
            Log.d("버그", "선생님소개업로드실패: ${exception.message}")
        }
    }
    //선생님 프사 업로드
    fun uploadTeacherProfileImage(uri: Uri, uid: String)
    {

        val storageReference = Firebase.storage.reference.child("teachers/$uid")
        val uploadTask = storageReference.putFile(uri)
        uploadTask.addOnSuccessListener {
        }.addOnFailureListener {
            Log.d("버그", "TeacherRepository에서 프사 업로드 못하는 중 : ${it.message}", it)
        }
    }
    //선생님 등록 트루 변경
    fun changeTeacherStatus(uid: String)
    {
        val data = hashMapOf(
            "teacher" to true
        )
        db.collection("users").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "TeacherRepository에서 user 선생님 true로 변경 못하고 있음: ${exception.message}")
        }
    }
    //선생님 정보 변경
    fun modifyTeacherInfo(fixInfo : String, fixName : String, uid: String)
    {
        var fixInfoArticle : String? = null
        when(fixInfo)
        {
            "닉네임" ->  fixInfoArticle ="nickname"
            "한줄소개" -> fixInfoArticle = "des"
            "학적 상태" ->fixInfoArticle = "status"
            "지역"->fixInfoArticle = "availableLocal"
            "수업 방식"->fixInfoArticle = "way"
            "과목"->fixInfoArticle = "subject"

        }
        val data = hashMapOf(
            fixInfoArticle to fixName
        )
        db.collection("teachers").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "TeacherRepository에서 정보 변경 못하는 중 : ${exception.message}")
        }
    }
    //선생님 정보 변경(배열)
    fun modifyTeacherInfoArray(fixInfo : String, fixName : List<String>, uid: String)
    {
        var fixInfoArticle : String? = null
        when(fixInfo)
        {
            "지역"->fixInfoArticle = "availableLocal"
            "과목"->fixInfoArticle = "subject"
        }
        val data = hashMapOf(
            fixInfoArticle to fixName
        )
        db.collection("teachers").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "TeacherRepository에서 정보 변경 못하는 중 : ${exception.message}")
        }
    }
}