package com.example.god_of_teaching.model.repository

import android.net.Uri
import android.util.Log
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
//학원 데이터 파이어 베이스로 올리는 레포지토리
class AcademyRepository {
    private val db = Firebase.firestore
    //학원 uid, 닉네임, 한줄 소개, 수업 대상 파이어 베이스로 업로드
    fun uploadAcademyInfo(uid: String, nickname: String, des: String, classAge: String)
    {
        val academyInfo = AcademyInfo(
            uid,
            nickname,
            des,
            classAge
        )
        db.collection("academys").document(uid).set(academyInfo).addOnSuccessListener {
        }.addOnFailureListener { exception ->
            Log.d("버그", ": AcademyRepository에서 uid, 닉네임, 한줄 소개, 수업 대상 ${exception.message}")
        }
    }
    //파이어스토리지에 학원 프로필 사진 업로드
    fun uploadAcademyProfileImage(uri: Uri, uid: String)
    {
        val storageReference = Firebase.storage.reference.child("academys/$uid")
        val uploadTask = storageReference.putFile(uri)
        Log.d("12321",uid)
        uploadTask.addOnSuccessListener {
        }.addOnFailureListener {
            Log.d("버그", "AcademyRepository에서 프사 업로드 실패: ${it.message}", it)
        }
    }
    //파이어스토리지에 학원 등록증 사진 업로드
    fun uploadAcademyAuthImage(uri: Uri, uid: String)
    {
        val storageReference = Firebase.storage.reference.child("academyAuth/$uid")
        val uploadTask = storageReference.putFile(uri)
        Log.d("12321",uid)
        uploadTask.addOnSuccessListener {
        }.addOnFailureListener {
            Log.d("버그", "AcademyRepository에서 학원등록증 사진 업로드 실패: ${it.message}", it)
        }
    }
    //학원 상세 정보 파이어베이스에 업로드
    fun uploadAcademyLocal(uid : String, localNum : String, local : String, detailAddress : String)
    {
        val data = hashMapOf(
            "localNumber" to localNum,
            "local" to local,
            "detailAddress" to detailAddress
        )
        db.collection("academys").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener { exception ->
            Log.d("버그", "AcademyRepository에서 지역 업로드 실패: ${exception.message}")
        }
    }
    //검색될 학원 주소 업로드
    fun uploadSearchedAddressAcademy(uid: String, searchedLocal: List<String>)
    {
        val data = hashMapOf(
            "searchedLocal" to searchedLocal
        )
        db.collection("academys").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "AcademyRepository에서 검색 될 지역 업로드 실패: ${exception.message}")
        }
    }
    //학원 수강 과목 파이어 베이스에 업로드
    fun uploadAcademySubject(subjectList: List<String>, uid:String)
    {
        val data = hashMapOf(
            "subject" to subjectList
        )
        db.collection("academys").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "AcademyRepository에서 과목 업로드 실패: ${exception.message}")
        }
    }
    //학원 소개 파이어 베이스에 업로드
    fun uploadAcademyIntroduce(introduce: String, uid:String)
    {
        val data = hashMapOf(
            "introduce" to introduce
        )
        db.collection("academys").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "AcademyRepository에서 소개 업로드 못하고 있음: ${exception.message}")
        }
    }
    //학원 등록 true로 변환
    fun changeAcademyStatus(uid: String)
    {
        val data = hashMapOf(
            "academy" to true
        )
        db.collection("users").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 선생님 true로 변경 못하고 있음: ${exception.message}")
        }
    }
    fun modifyAcademyInfo(fixInfo: String, fixName: String, uid: String)
    {
        var fixInfoArticle : String? = null
        when(fixInfo)
        {
            "한줄소개" ->  fixInfoArticle ="des"

            "학원소개"->fixInfoArticle = "introduce"
            "검색 지역"->fixInfoArticle = "searchedLocal"


        }
        val data = hashMapOf(
            fixInfoArticle to fixName
        )
        db.collection("academys").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "StudentRepository에서 정보 변경 못하는 중 : ${exception.message}")
        }
    }
    fun modifyAcademyInfoArray(fixInfo: String, fixName: List<String>, uid: String)
    {
        var fixInfoArticle : String? = null
        when(fixInfo)
        {
            "과목" ->fixInfoArticle = "subject"
            "수업 대상" -> fixInfoArticle = "classAge"
            "검색 지역"->fixInfoArticle = "searchedLocal"
        }
        val data = hashMapOf(
            fixInfoArticle to fixName
        )
        db.collection("academys").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "academyRepository에서 정보 변경 못하는 중 : ${exception.message}")
        }
    }
}