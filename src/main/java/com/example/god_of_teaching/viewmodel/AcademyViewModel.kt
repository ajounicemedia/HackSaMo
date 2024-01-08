package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.repository.AcademyRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


//학원 정보 로컬에 넣기 + 학원 레포지토리 호출
@HiltViewModel
class AcademyViewModel @Inject constructor
    (private val academyRepository : AcademyRepository,
     private val userDataStoreHelper: UserDataStoreHelper,
     private val academyDatastore: AcademyDatastoreHelper,
     application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore
    //캐시 삭제후 정보 다시 불러오기
    fun getTeacherAllData(uid: String, context: Context)
    {
        val userRef = uid?.let { db.collection("academys").document(it).get() }
        userRef?.addOnSuccessListener { document ->
            if (document != null) {
                val name = document.getString("name").toString()
                val des = document.getString("des").toString()
                val classAge = document.getString("classAge").toString()
                val localNumber = document.getString("localNumber").toString()
                val local = document.getString("local").toString()
                val detailAddress = document.getString("detailAddress").toString()
               val introduce = document.getString("introduce").toString()
               val searchedLocal = document.getString("searchedLocal").toString()
               // val subject = document.getString("subject").toString()
                val subjectList = document.get("subject") as? List<String> ?: emptyList()
                val subject = subjectList.joinToString(", ")
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertNickname(name)
                    academyDatastore.insertDes(des)
                    academyDatastore.insertClassAge(classAge)
                    academyDatastore.insertLocalNumber(localNumber)
                    academyDatastore.insertLocal(local)
                    academyDatastore.insertDetailAddress(detailAddress)
                    academyDatastore.insertSubject(subject)
                    academyDatastore.insertIntroduce(introduce)
                    academyDatastore.insertSearchedLocal(searchedLocal)
                }
            }
            else {
                Log.d("버그", "학원뷰모델에서 uid 없음")
            }}?.addOnFailureListener { exception ->
            Log.d("버그", "학원뷰모델에서 uid 못 찾고 있음", exception)
        }
        //사진 로컬에 저장
        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("academys/$uid")
        val profileImageRefAuth = storageReference.child("academyAuth/$uid")//사용유무 아직 모름
        val localFile = File(context?.filesDir, "$uid" + "academy.jpg")
        val localFileAuth = File(context?.filesDir, "$uid" + "academyAuth/.jpg")//사용유무 아직 모름
        //학원프사
        profileImageRef.getFile(localFile).addOnSuccessListener {
        }.addOnFailureListener { exception ->
            Log.d("버그","학원뷰모델에서 사진 못 받아오는 중 ")
        }
        //학원 등록증 프사
        profileImageRefAuth.getFile(localFileAuth).addOnSuccessListener {
        }.addOnFailureListener { exception ->
            Log.d("버그","학원뷰모델에서 사진 못 받아오는 중 ")
        }
    }
    //파이어 베이스에 데이터 업로드
    fun uploadAcademyInfo(des:String,classAge: List<String>) {
        val combinedString = classAge.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val nickname = userDataStoreHelper.getNickname.first()
            academyRepository.uploadAcademyInfo(uid, nickname, des, combinedString )

        }
    }
    //로컬에 데이터 업로드
    fun uploadAcademyInfoToLocal(des:String,classAge: List<String>){
        val combinedString = classAge.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertDes(des)
            academyDatastore.insertClassAge(combinedString)
        }
    }
    //파이어 베이스에 데이터 업로드
    fun uplodeProfileImage(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.uploadAcademyProfileImage(uri, uid)
            }
        }

    }
    //학원 등록증 업로드
    fun uploadAuthImage(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.uploadAcademyAuthImage(uri, uid)
            }
        }

    }
    //로컬에 데이터 업로드
    fun uploadLocalData(localNum : String,local : String,detailAddress : String)
    {

        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.uploadAcademyLocal(uid,localNum, local, detailAddress)
            }
        }
    }
    //로컬에 데이터 업로드
    fun uploadLocalDataToLocal(localNum : String,local : String,detailAddress : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertLocalNumber(localNum)
            academyDatastore.insertLocal(local)
            academyDatastore.insertDetailAddress(detailAddress)
        }
    }
    //검색될 지역 업로드
    fun uploadSearchedLocalList(searchedLocal: List<String>)
    {
        val combinedString = searchedLocal.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.uploadSearchedAddressAcademy(uid, searchedLocal)
                academyDatastore.insertSearchedLocal(combinedString)
            }
        }
    }
    //파이어 베이스에 데이터 업로드
    fun uploadSubject(subjectList: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.uploadAcademySubject(subjectList, uid)
            }
        }
    }
    //로컬에 데이터 업로드
    fun uploadSubjectToLocal(subjectList: List<String>)
    {
        val combinedString = subjectList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
        academyDatastore.insertSubject(combinedString)
        }

    }
    //파이어 베이스에 데이터 업로드
    fun uploadIntroduce(introduce : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.uploadAcademyIntroduce(introduce, uid)
            }
        }
    }
    //로컬에 데이터 업로드
    fun uploadIntroduceToLocal(introduce : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertIntroduce(introduce)
        }

    }
    //선생님 상태 변경
    fun changeAcademyStatus()
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.changeAcademyStatus(uid)
            }
        }
    }
    //이미지 로컬에 업로드
    fun uplodeAcademyProfileImageToLocal(uri: Uri, context: Context)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri!!)
                val filename = uid+"academy.jpg"
                val file = File(context?.filesDir, filename)
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
            }
        }
    }

    fun modifyAcademyInfo(fixInfo: String, fixName: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.modifyAcademyInfo(fixInfo, fixName, uid)
            }
        }
        when(fixInfo) {
            "한줄소개" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertDes(fixName)
                }
            "학원 소개" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertIntroduce(fixName)
                }


        }
    }
    fun modifyAcademyInfoArray(fixInfo: String, fixName: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                academyRepository.modifyAcademyInfoArray(fixInfo, fixName, uid)
            }
        }
        val combinedString = fixName.joinToString(", ")
        when(fixInfo) {
            "과목" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertSubject( combinedString)
                }
            "수업 대상" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertClassAge( combinedString)
                }
            "검색 지역" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertSearchedLocal(combinedString)
                }

        }
    }
    //데이터바인딩 위해서
    val readAcademyName = academyDatastore.getNickname.asLiveData()
    val readDes = academyDatastore.getDes.asLiveData()
    val readClassAge = academyDatastore.getClassAge.asLiveData()
    val readLocal = academyDatastore.getLocal.asLiveData()
    val readDetailAddress = academyDatastore.getDetailAddress.asLiveData()
    val readSubject = academyDatastore.getSubject.asLiveData()
    val readIntroduce = academyDatastore.getIntroduce.asLiveData()
    val readAvailableLocal = academyDatastore.getSearchedLocal.asLiveData()

}