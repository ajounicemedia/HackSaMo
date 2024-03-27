package god.example.god_of_teaching.model.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject



//선생님 데이터 관리 레포지토리
class TeacherRepository @Inject constructor(
    private val teacherDataStoreHelper: TeacherDataStoreHelper,
    private val userDataStoreHelper: UserDataStoreHelper
){
    private val db = Firebase.firestore

    //선생님 uid, 닉네임, 한줄 소개, 출생년도, 성별, 학교, 학교 위치, 전공, 재학상태, 수업방식 파이어베이스에 업로드
    fun uploadTeacherInfo(des: String, bornYear: String, gender: String, campus: String, campusLocal: String,
        major: String, status: String, way: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val nickname = userDataStoreHelper.getNickname.first()
            val uid = userDataStoreHelper.getUid.first()
            val teacherInfo = TeacherInfo(
                uid, nickname, des, bornYear, gender, campus, campusLocal, major, status, way
            )
            db.collection("teachers").document(uid).set(teacherInfo)
                .addOnFailureListener { exception ->
                    Log.d("버그", "TeacherRepository에서 선생님 정보(가장 많은 곳) 업로드실패: ${exception.message}")
                }
        }
    }
    //선생님 uid, 닉네임, 한줄 소개, 출생년도, 성별, 학교, 학교 위치, 전공, 재학상태, 수업방식 로컬에 저장
    fun storeTeacherInfo(des: String, bornYear: String, gender: String, campus: String, campusLocal: String,
                         major: String, status: String, way: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val nickname = userDataStoreHelper.getNickname.first()
            val dataMap = mapOf("nickname" to nickname, "des" to des, "bornYear" to bornYear, "campus" to campus,
                    "gender" to gender, "campusLocal" to campusLocal, "major" to major, "status" to status, "way" to way)
            teacherDataStoreHelper.insertData(dataMap)
        }
    }
    //선생님 프사 파이어베이스에 업로드
    fun uploadTeacherProfileImage(uri: Uri)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val storageReference = Firebase.storage.reference.child("teachers/$uid")
            val uploadTask = storageReference.putFile(uri)
            uploadTask.addOnFailureListener {
                Log.d("버그", "TeacherRepository에서 프사 업로드 못하는 중 : ${it.message}", it)
            }
        }
    }
    //선생님 프사 로컬에 저장
    fun storeTeacherProfileImage(uri: Uri, context: Context)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri!!)
            val filename = uid + "teacher.jpg"
            val file = File(context?.filesDir, filename)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        }
    }
    //파이어스토리지에 학원 등록증 사진 업로드
    fun uploadTeacherAuthImage(uri: Uri)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val storageReference = Firebase.storage.reference.child("teacherAuth/$uid")
            val uploadTask = storageReference.putFile(uri)
            uploadTask
                .addOnFailureListener {
                    Log.d("버그", "TeacherRepository에서 학원등록증 사진 업로드 실패: ${it.message}", it)
                }
        }
    }
    //선생님 활동 가능한 지역 파이어베이스에 업로드
    fun uploadTeacherAvailableLocal(localList: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "availableLocal" to localList
            )
            db.collection("teachers").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "TeacherRepository에서 지역업로드실패: ${exception.message}")
                }
        }
    }
    //선생님 활동 가능한 지역 로컬에 저장
    fun storeTeacherAvailableLocal(localList: List<String>)
    {
        val combinedString = localList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            teacherDataStoreHelper.insertAvailableLocal(combinedString)
        }
    }
    //선생님 수업할 과목 파이어베이스에 업로드
    fun uploadTeacherSubject(subjectList: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "subject" to subjectList
            )
            db.collection("teachers").document(uid!!).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "TeacherRepository에서과목업로드실패: ${exception.message}")
                }
        }
    }
    //선생님 수업할 과목 로컬에 저장
    fun storeTeacherSubject(subjectList: List<String>)
    {
        val combinedString = subjectList.joinToString(", ")
        CoroutineScope(Dispatchers.IO).launch {
            teacherDataStoreHelper.insertSubject(combinedString)
        }
    }
    //선생님 상세 소개 파이어베이스에 업로드
    fun uploadTeacherIntroduce(introduce: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "introduce" to introduce
            )
            db.collection("teachers").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener {exception ->
                    Log.d("버그", "선생님소개업로드실패: ${exception.message}")
                }
            }
    }
    //선생님 상세 소개 로컬에 저장
    fun storeTeacherIntroduce(introduce: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            teacherDataStoreHelper.insertIntroduce(introduce)
        }
    }
    //파이어베이스에서 선생님 등록 완료 처리 및 유저 상태 선생님으로 변경
    fun registerCompleteTeacher()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val userInfo = hashMapOf(

                "teacher" to "true",
                "status" to "teacher"
            )
            db.collection("users").document(uid).set(userInfo, SetOptions.merge())
                .addOnFailureListener {
                    Log.w("버그", "TeacherRepository에서 선생님 등록 완료 실패")
                }
        }
    }
    //로컬에서 선생님 등록 완료 처리 및 유저 상태 선생님으로 변경
    fun registerCompleteTeacherLocal()
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertStatus("teacher")
            userDataStoreHelper.insertTeacher("true")
        }
    }
    //선생님 정보 변경(파이어베이스)
    fun modifyTeacherInfo(fixInfo : String, fixName : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            var fixInfoArticle: String? = null
            when (fixInfo) {
                "닉네임" -> fixInfoArticle = "nickname"
                "한줄소개" -> fixInfoArticle = "des"
                "학적 상태" -> fixInfoArticle = "status"
                "지역" -> fixInfoArticle = "availableLocal"
                "수업 방식" -> fixInfoArticle = "way"
                "과목" -> fixInfoArticle = "subject"
            }
            val data = hashMapOf(
                fixInfoArticle to fixName
            )
            db.collection("teachers").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "TeacherRepository에서 정보 변경 못하는 중 : ${exception.message}")
                }
        }
    }

    //선생님 정보 변경(로컬)
    fun modifyTeacherInfoLocal(fixInfo : String, fixName : String)
    {
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
    //파이어베이스에서 선생님 정보 변경(배열)
    fun modifyTeacherInfoArray(fixInfo : String, fixName : List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            var fixInfoArticle: String? = null
            when (fixInfo) {
                "지역" -> fixInfoArticle = "availableLocal"
                "과목" -> fixInfoArticle = "subject"
            }
            val data = hashMapOf(
                fixInfoArticle to fixName
            )
            db.collection("teachers").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                Log.d("버그", "TeacherRepository에서 정보 변경 못하는 중 : ${exception.message}")
            }
        }
    }
    //로컬에서 선생님 정보 변경(배열)
    fun modifyTeacherInfoArrayLocal(fixInfo : String, fixName : String)
    {
        when(fixInfo) {
            "과목" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    teacherDataStoreHelper.insertSubject(fixName)
                }
            }
            "지역" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    teacherDataStoreHelper.insertAvailableLocal(fixName)
                }
            }
        }
    }
    //파이어베이스에서 선생님 정보 불러오기
    fun getTeacherData(uid: String): LiveData<TeacherInfo> {

        val teacherInfo = MutableLiveData<TeacherInfo?>()

        CoroutineScope(Dispatchers.IO).launch {

            try {
                val documentSnapshot = db.collection("teachers").document(uid).get().await()
                var info = documentSnapshot.toObject(TeacherInfo::class.java)
                if (info != null) {
                    while (info!!.nickname == null || info.des == null || info.bornYear == null || info.gender == null
                        || info.campus == null|| info.campusLocal == null|| info.major == null|| info.status == null
                        || info.way == null|| info.availableLocal == null|| info.subject == null|| info.introduce == null
                    ) {
                        try {
                            val documentSnapshot = db.collection("teachers").document(uid).get().await()
                            info = documentSnapshot.toObject(TeacherInfo::class.java)
                        }
                        catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Log.d("버그", "TeacherRepository에서 선생님 정보 못 받아 오는중: ${e.message}")
                                teacherInfo.value = null
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    teacherInfo.value = info
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("버그", "TeacherRepository에서 선생님 정보 못 받아 오는중: ${e.message}")
                    teacherInfo.value = null
                }
            }
        }

        return teacherInfo as LiveData<TeacherInfo>
    }
    //파이어베이스에서 선생님 프사 불러와서 로컬에 넣기
    fun getTeacherProfileImage(uid: String,context: Context) {

        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("teachers/$uid")
        val localFile = File(context.filesDir, uid + "teacher.jpg")
        profileImageRef.getFile(localFile).addOnFailureListener { exception ->
            Log.d("버그", "TeacherRepository에서 사진 못 받아오는 중 ")
        }
    }

    //선생님 로컬 데이터 조회
    val teacherDes : Flow<String> get() = teacherDataStoreHelper.getDes
    val teacherBornYear : Flow<String> get()= teacherDataStoreHelper.getBornYear
    val teacherGender : Flow<String> get()= teacherDataStoreHelper.getGender
    val teacherCampus : Flow<String> get()= teacherDataStoreHelper.getCampus
    val teacherCampusLocal : Flow<String> get()= teacherDataStoreHelper.getCampusLocal
    val teacherStatus : Flow<String> get()= teacherDataStoreHelper.getStatus
    val teacherLocal : Flow<String> get()= teacherDataStoreHelper.getAvailableLocal
    val teacherSubject : Flow<String> get()= teacherDataStoreHelper.getSubject
    val teacherIntroduce : Flow<String> get()= teacherDataStoreHelper.getIntroduce
    val teacherWay : Flow<String> get()= teacherDataStoreHelper.getWay
}