package com.example.god_of_teaching.model.repository

import android.net.Uri
import android.util.Log
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
//학생 정보를 파이어베이스에 업로드하는 레포지토리
class StudentRepository {
    private val db = Firebase.firestore
    //학생 uid, 닉네임, 한줄소개, 출생년도, 성별, 수업방식 파이어 베이스에 업로드
    fun uploadStudentInfo(uid: String,nickname: String, des: String, bornYear: String, gender: String, way: String)
    {
        val studentInfo = StudentInfo(
            uid, nickname, des, bornYear, gender, way
        )
        db.collection("students").document(uid).set(studentInfo).addOnSuccessListener {
        }.addOnFailureListener { exception ->
            Log.d("버그", "StudentRepository에서 학생 uid, 닉네임, 한줄소개, 출생년도, 성별, 수업방식 업로드 못하는 중: ${exception.message}")
        }
    }
    //학생 프로필 사진 파이어 베이스에 업로드
    fun uploadStudentProfileImage(uri: Uri, uid: String)
    {
        val storageReference = Firebase.storage.reference.child("students/$uid")
        val uploadTask = storageReference.putFile(uri)
        uploadTask.addOnSuccessListener {
        }.addOnFailureListener {
            Log.d("버그", "StudentRepository에서 학생 프사 업로드 실패: ${it.message}", it)
        }
    }
    //학생 주소 파이어베이스에 업로드
    fun uploadStudentLocal(localList: List<String>, uid:String)
    {
        val data = hashMapOf(
            "availableLocal" to localList
        )
        db.collection("students").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 학생 주소 업로드 못하는 중: ${exception.message}")
        }
    }
    fun uploadStudentSubject(subjectList: List<String>, uid:String)
    {
        val data = hashMapOf(
            "subject" to subjectList
        )
        db.collection("students").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 과목업로드실패: ${exception.message}")
        }
    }
    //학생 소개 파이어베이스에 업로드
    fun uploadStudentIntroduce(introduce: String, uid:String)
    {
        val data = hashMapOf(
            "introduce" to introduce
        )
        db.collection("students").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 소개업로드실패: ${exception.message}")
        }
    }
    //학생 등록 트루 변경
    fun changeStudentStatus(uid: String)
    {
        val data = hashMapOf(
            "student" to true
        )
        db.collection("users").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 선생님 true로 변경 못하고 있음: ${exception.message}")
        }
    }
    fun modifyStudentInfo(fixInfo : String, fixName : String, uid: String)
    {
        var fixInfoArticle : String? = null
        when(fixInfo)
        {
            "닉네임" ->  fixInfoArticle ="nickname"
            "한줄소개" -> fixInfoArticle = "des"
            "수업 방식" ->fixInfoArticle = "way"
            "자기소개"->fixInfoArticle = "introduce"

        }
        val data = hashMapOf(
            fixInfoArticle to fixName
        )
        db.collection("students").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 정보 변경 못하는 중 : ${exception.message}")
        }
    }
    fun modifyStudentInfoArray(fixInfo : String, fixName : List<String>, uid: String)
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
        db.collection("students").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 정보 변경 못하는 중 : ${exception.message}")
        }
    }

}