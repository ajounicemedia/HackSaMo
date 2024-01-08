package com.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.repository.TeacherRepository
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


//선생님 레포지토리 호출 + 로컬에 데이터 삽입
@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    private val userDataStoreHelper: UserDataStoreHelper,
    private val teacherDataStoreHelper: TeacherDataStoreHelper,
    application: Application
) : AndroidViewModel(application) {
    private val db = Firebase.firestore


    //캐시 날라 갔을 때 데이터 불러오기
    fun getTeacherAllData(uid: String, context: Context)
    {
        //이 과정을 레포지토리로 보낼지 고민
        val userRef = uid?.let { db.collection("teachers").document(it).get() }
        userRef?.addOnSuccessListener { document ->
            if (document != null) {
                val nickname = document.getString("nickname").toString()
                val des = document.getString("des").toString()
                val bornYear= document.getString("bornYear").toString()
                val campus = document.getString("campus").toString()
                val campusLocal = document.getString("campusLocal").toString()
                val major = document.getString("major").toString()
                val status = document.getString("status").toString()
                val way= document.getString("way").toString()
                val introduce = document.getString("introduce").toString()
                val gender = document.getString("gender").toString()
//                val subject = document.getString("subject").toString()
//                val availableLocal= document.getString("availableLocal").toString()
                val subjectList = document.get("subject") as? List<String> ?: emptyList()
                val availableLocalList  = document.get("availableLocal") as? List<String> ?: emptyList()
                val subject = subjectList.joinToString(", ")
                val availableLocal= availableLocalList.joinToString(", ")
                CoroutineScope(Dispatchers.IO).launch {
                    teacherDataStoreHelper.insertNickname(nickname)
                    teacherDataStoreHelper.insertDes(des)
                    teacherDataStoreHelper.insertBornYear(bornYear)
                    teacherDataStoreHelper.insertCampus(campus)
                    teacherDataStoreHelper.insertCampusLocal(campusLocal)
                    teacherDataStoreHelper.insertMajor(major)
                    teacherDataStoreHelper.insertStatus(status)
                    teacherDataStoreHelper.insertWay(way)
                    teacherDataStoreHelper.insertAvailableLocal(availableLocal)
                    teacherDataStoreHelper.insertSubject(subject)
                    teacherDataStoreHelper.insertIntroduce(introduce)
                    teacherDataStoreHelper.insertGender(gender)
                }
            }
        else {
        Log.d("버그", "학생뷰모델에서 uid 없음")
    }}?.addOnFailureListener { exception ->
        Log.d("버그", "선생뷰모델에서 uid 못 찾고 있음", exception)
    }
        //사진 로컬에 저장
        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("teachers/$uid")
        val localFile = File(context?.filesDir, "$uid" + "teacher.jpg")

        profileImageRef.getFile(localFile).addOnSuccessListener {

            // Image successfully downloaded and saved to local file
        }.addOnFailureListener { exception ->
            Log.d("버그","티쳐뷰모델에서 사진 못 받아오는 중 ")
            // Handle any errors
        }
    }


    fun uploadTeacherInfoToLocal(des: String, bornYear: String,
        gender: String, campus: String, campusLocal: String,
        major: String, status: String, way: String) {
         CoroutineScope(Dispatchers.IO).launch {
            //teacherDataStoreHelper.insertNickname(nickname)
            teacherDataStoreHelper.insertDes(des)
            teacherDataStoreHelper.insertBornYear(bornYear)
            teacherDataStoreHelper.insertCampus(campus)
            teacherDataStoreHelper.insertCampusLocal(campusLocal)
            teacherDataStoreHelper.insertMajor(major)
            teacherDataStoreHelper.insertStatus(status)
            teacherDataStoreHelper.insertWay(way)
             teacherDataStoreHelper.insertGender(gender)
        }
    }


    //파이어 베이스에 떼이터 업로드
    fun uploadTeacherInfo(des: String, bornYear: String,
                          gender: String, campus: String, campusLocal: String,
                          major: String, status: String, way: String) {
         CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
             val nickname = userDataStoreHelper.getNickname.first()
                teacherRepository.uploadTeacherInfo(
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

        }
    }

    //과외가능한 지역 업로드
    fun uploadTeacherAvailableLocal(localList: List<String>)
    {
        //val combinedString = localList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
                teacherRepository.uploadTeacherLocal(localList, uid)

        }
    }

    fun uploadTeacherAvailableLocalToLocal(localList: List<String>)
    {
        val combinedString = localList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            teacherDataStoreHelper.insertAvailableLocal(combinedString)
        }

    }
    //파이어 베이스에 떼이터 업로드
    fun uploadTeacherSubject(subjectList: List<String>)
    {


         CoroutineScope(Dispatchers.IO).launch {
             val uid = userDataStoreHelper.getUid.first()
                teacherRepository.uploadTeacherSubject(subjectList, uid)
            }

    }
    //선생님 과목 로컬에 저장
    fun uploadTeacherSubjectLocal(subjectList: List<String>)
    {
        val combinedString = subjectList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            teacherDataStoreHelper.insertSubject(combinedString)
        }

    }
    //파이어 베이스에 떼이터 업로드
    fun uploadTeacherIntroduce(introduce : String)
    {
         CoroutineScope(Dispatchers.IO).launch {
             val uid = userDataStoreHelper.getUid.first()
                teacherRepository.uploadTeacherIntroduce(introduce, uid)

        }
    }
    fun uploadTeacherIntroduceLocal(introduce: String)
    {
         CoroutineScope(Dispatchers.IO).launch {
            teacherDataStoreHelper.insertIntroduce(introduce)
        }
    }
    //파이어 베이스에 떼이터 업로드
    fun uplodeTeacherProfileImage(uri: Uri)
    {
         CoroutineScope(Dispatchers.IO).launch {
             val uid = userDataStoreHelper.getUid.first()
                teacherRepository.uploadTeacherProfileImage(uri,uid)
        }
    }
    //이미지 로컬에 업로드
    fun uplodeTeacherProfileImageToLocal(uri: Uri, context: Context)
    {
        CoroutineScope(Dispatchers.IO).launch {
                val uid = userDataStoreHelper.getUid.first()
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri!!)
                val filename = uid+"teacher.jpg"
                val file = File(context?.filesDir, filename)
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
        }
    }



    //유저에서 선생님 체크 트루
    fun changeTeacherStatus()
    {

        CoroutineScope(Dispatchers.IO).launch {
                val uid = userDataStoreHelper.getUid.first()
                teacherRepository.changeTeacherStatus(uid)
        }
    }

    //선생님 정보 변경
    fun modifyTeacherInfo(fixInfo: String, fixName: String)
    {

        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                teacherRepository.modifyTeacherInfo(fixInfo, fixName, uid)

            }
        }
            when(fixInfo) {
                "닉네임" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        teacherDataStoreHelper.insertNickname(fixName)
                    }
                }

                "한줄소개" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        teacherDataStoreHelper.insertDes(fixName)
                    }
                }

                "학적 상태" -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            teacherDataStoreHelper.insertStatus(fixName)
                        }
                }

                "지역" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        teacherDataStoreHelper.insertAvailableLocal(fixName)
                    }
                }

                "소개" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        teacherDataStoreHelper.insertAvailableLocal(fixName)
                    }
                }

                "과목" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        teacherDataStoreHelper.insertSubject(fixName)
                    }
                }

                "수업 방식" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        teacherDataStoreHelper.insertWay(fixName)
                    }
                }
            }


    }
    fun modifyTeacherInfoArray(fixInfo: String, fixName: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                teacherRepository.modifyTeacherInfoArray(fixInfo, fixName, uid)
            }

        }
        val combinedString = fixName.joinToString(", ")
        when(fixInfo) {
            "과목" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    teacherDataStoreHelper.insertSubject(combinedString)
                }
            }

            "지역" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    teacherDataStoreHelper.insertAvailableLocal(combinedString)
                }
            }
        }
    }
    //데이터바인딩용
    val readNickname = teacherDataStoreHelper.getNickname.asLiveData()
    val readDes = teacherDataStoreHelper.getDes.asLiveData()
    val readBornYear = teacherDataStoreHelper.getBornYear.asLiveData()
    val readGender = teacherDataStoreHelper.getGender.asLiveData()
    val readCampus = teacherDataStoreHelper.getCampus.asLiveData()
    val readCampusLocal = teacherDataStoreHelper.getCampusLocal.asLiveData()
    val readStatus = teacherDataStoreHelper.getStatus.asLiveData()
    val readLocal = teacherDataStoreHelper.getAvailableLocal.asLiveData()
    val readSubject = teacherDataStoreHelper.getSubject.asLiveData()
    val readIntroduce = teacherDataStoreHelper.getIntroduce.asLiveData()
    val readWay = teacherDataStoreHelper.getWay.asLiveData()

}

