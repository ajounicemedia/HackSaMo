package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.repository.StudentRepository
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

//학생 레포지토리 호출 + 로컬에 학생데이터 삽입
@HiltViewModel
class StudentViewModel@Inject constructor(
    private val userDataStoreHelper : UserDataStoreHelper,
    private val studentRepository : StudentRepository,
    private val studentDatastoreHelper : StudentDatastoreHelper,
     application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore



    //캐시 없을 때 데이터 불러오기
    fun getStudentAllData(uid: String, context: Context)
    {
        val userRef = uid?.let { db.collection("students").document(it).get() }
        userRef?.addOnSuccessListener { document ->
            if (document != null) {
                val nickname = document.getString("nickname").toString()
                val des = document.getString("des").toString()
                val bornYear = document.getString("bornYear").toString()
                val way= document.getString("way").toString()
                val introduce = document.getString("introduce").toString()
                val gender = document.getString("gender").toString()
                //val subject = document.getString("subject").toString()
                //val availableLocal= document.getString("availableLocal").toString()
                val subjectList = document.get("subject") as? List<String> ?: emptyList()
                val availableLocalList  = document.get("availableLocal") as? List<String> ?: emptyList()
                val subject = subjectList.joinToString(", ")
                val availableLocal= availableLocalList.joinToString(", ")
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertNickname(nickname)
                    studentDatastoreHelper.insertWay(way)
                    studentDatastoreHelper.insertDes(des)
                    studentDatastoreHelper.insertBornYear(bornYear)
                    studentDatastoreHelper.insertAvailableLocal(availableLocal)
                    studentDatastoreHelper.insertSubject(subject)
                    studentDatastoreHelper.insertIntroduce(introduce)
                    studentDatastoreHelper.insertGender(gender)
                    }
                }
            else {
                Log.d("버그", "학생뷰모델에서 uid 없음")
            }}
            ?.addOnFailureListener { exception ->
                    Log.d("버그", "학생뷰모델에서 uid 못 찾고 있음", exception)
                }

        //사진 로컬에 저장
        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("students/$uid")
        val localFile = File(context?.filesDir, "$uid" + "student.jpg")

        profileImageRef.getFile(localFile).addOnSuccessListener {

            // Image successfully downloaded and saved to local file
        }.addOnFailureListener { exception ->
            Log.d("버그","학생뷰모델에서 사진 못 받아오는 중 ")
            // Handle any errors
        }

    }
    //파이어베이스에 데이터 업로드
    fun uploadStudentInfo(des: String, bornYear: String, gender: String, way: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid =userDataStoreHelper.getUid.first()
            val nickname = userDataStoreHelper.getNickname.first()
            studentRepository.uploadStudentInfo(uid, nickname, des, bornYear, gender, way)

        }

    }
    //로컬에 데이터 업로드
    fun uploadStudentInfoLocal(des: String, bornYear: String, gender: String, way: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            //studentDatastoreHelper.insertNickname(nickname)
            studentDatastoreHelper.insertWay(way)
            studentDatastoreHelper.insertDes(des)
            studentDatastoreHelper.insertBornYear(bornYear)
            studentDatastoreHelper.insertGender(gender)
        }

    }
    //파이어베이스에 데이터 업로드
    fun uplodeProfileImage(uri: Uri)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                studentRepository.uploadStudentProfileImage(uri,uid)
            }
        }
    }
    //파이어베이스에 데이터 업로드
    fun uploadStudentAvailableLocal(localList: List<String>)
    {
        val combinedString = localList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                studentRepository.uploadStudentLocal(localList, uid)
            }
        }
    }
    //로컬에 데이터 업로드
    fun uploadStudentAvailableLocalToLocal(localList: List<String>)
    {
        val combinedString = localList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            studentDatastoreHelper.insertAvailableLocal(combinedString)
        }
    }
    //파이어베이스에 데이터 업로드
    fun uploadStudentSubject(subjectList: List<String>)
    {
        val combinedString = subjectList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                studentRepository.uploadStudentSubject(subjectList, uid)
            }
        }
    }
    //로컬에 데이터 업로드
    fun uploadStudentSubjectLocal(subjectList: List<String>)
    {
        val combinedString = subjectList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            studentDatastoreHelper.insertSubject(combinedString)
        }

    }
    //파이어베이스에 데이터 업로드
    fun uplodeStudentIntroduce(introduce : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                studentRepository.uploadStudentIntroduce(introduce, uid)
            }
        }
    }
    //로컬에 데이터 업로드
    fun uplodeStudentIntroduceLocal(introduce : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
               studentDatastoreHelper.insertIntroduce(introduce)
        }
    }
    //유저에서 학생 체크 트루
    fun changeStudentStatus()
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                studentRepository.changeStudentStatus(uid)
            }
        }


    }

    //이미지 로컬에 업로드
    fun uplodeStudentProfileImageToLocal(uri: Uri, context: Context)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri!!)
                val filename = uid+"student.jpg"
                val file = File(context?.filesDir, filename)
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
            }
        }
    }
    fun modifyStudentInfo(fixInfo: String, fixName: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                studentRepository.modifyStudentInfo(fixInfo, fixName, uid)
            }
        }
        when(fixInfo) {
            "닉네임" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertNickname(fixName)
                }
            "한줄소개" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertDes(fixName)
                }
            "수업 방식" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertWay(fixName)
                }
            "자기소개" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertIntroduce(fixName)
                }

        }
    }
    fun modifyStudentInfoArray(fixInfo: String, fixName: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                studentRepository.modifyStudentInfoArray(fixInfo, fixName, uid)
            }
        }
        val combinedString = fixName.joinToString(", ")
        when(fixInfo) {

            "지역" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertAvailableLocal(combinedString)
                }
            "과목" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertSubject(combinedString)
                }

        }
    }
    //데이타바인딩용
    val readNickname = studentDatastoreHelper.getNickname.asLiveData()
    val readDes = studentDatastoreHelper.getDes.asLiveData()
    val readBornYear = studentDatastoreHelper.getBornYear.asLiveData()
    val readGender = studentDatastoreHelper.getGender.asLiveData()
    val readWay = studentDatastoreHelper.getWay.asLiveData()
    val readLocal = studentDatastoreHelper.getAvailableLocal.asLiveData()
    val readSubject = studentDatastoreHelper.getSubject.asLiveData()
    val readIntroduce = studentDatastoreHelper.getIntroduce.asLiveData()




}