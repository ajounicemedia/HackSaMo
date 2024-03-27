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
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.datastore.StudentDatastoreHelper
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

//학생 데이터 관리 레포지토리
class StudentRepository@Inject constructor(
    private val studentDatastoreHelper : StudentDatastoreHelper,
    private val userDataStoreHelper : UserDataStoreHelper
)
    {
    private val db = Firebase.firestore
    //학생 uid, 닉네임, 한줄소개, 출생년도, 성별, 수업방식 파이어베이스에 업로드
    fun uploadStudentInfo(des: String, bornYear: String, gender: String, way: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val nickname = userDataStoreHelper.getNickname.first()
            val uid = userDataStoreHelper.getUid.first()
            val studentInfo = StudentInfo(
                uid, nickname, des, bornYear, gender, way
            )
            db.collection("students").document(uid).set(studentInfo)
                .addOnFailureListener { exception ->
                    Log.d("버그", "StudentRepository에서 학생 uid, 닉네임, 한줄소개, 출생년도, 성별, 수업방식 업로드 못하는 중: ${exception.message}")
                }
        }
    }
    //학생 uid, 닉네임, 한줄소개, 출생년도, 성별, 수업방식 로컬에 저장
    fun storeStudentInfo(des: String, bornYear: String, gender: String, way: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val nickname = userDataStoreHelper.getNickname.first()
            val dataMap = mapOf("nickname" to nickname,"des" to des,"bornYear" to bornYear,"gender" to gender,"way" to way)
            studentDatastoreHelper.insertData(dataMap)
        }
    }
    //학생 프로필 사진 파이어 베이스에 업로드
    fun uploadStudentProfileImage(uri: Uri)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val storageReference = Firebase.storage.reference.child("students/$uid")
            val uploadTask = storageReference.putFile(uri)
            uploadTask.addOnFailureListener {
                    Log.d("버그", "StudentRepository에서 학생 프사 업로드 실패: ${it.message}", it)
                }
        }
    }
    //학생 프로필 사진 로컬에 저장
    fun storeStudentProfileImage(uri: Uri,context : Context)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri!!)
            val filename = uid + "student.jpg"
            val file = File(context?.filesDir, filename)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        }
    }

    //학생 활동 가능한 지역 파이어베이스에 업로드
    fun uploadStudentLocal(localList: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "availableLocal" to localList
            )
            db.collection("students").document(uid).set(data, SetOptions.merge())
               .addOnFailureListener { exception ->
                Log.d("버그", "StudentRepository에서 학생 주소 업로드 못하는 중: ${exception.message}")
            }
        }
    }
    //학생 활동 가능한 지역 로컬에 저장
    fun storeStudentLocal(localList:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            studentDatastoreHelper.insertAvailableLocal(localList)
        }
    }
    //학생이 수강 원하는 과목 파이어베이스에 저장
    fun uploadStudentSubject(subjectList: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "subject" to subjectList
            )
            db.collection("students").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                Log.d("버그", "StudentRepository에서 과목업로드실패: ${exception.message}")
            }
        }
    }
    //학생이 수강 원하는 과목 로컬에 저장
    fun storeStudentSubject(subjectList: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            studentDatastoreHelper.insertSubject(subjectList)
        }
    }
    //학생 상세 소개 파이어베이스에 업로드
    fun uploadStudentIntroduce(introduce: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "introduce" to introduce
            )
            db.collection("students").document(uid).set(data, SetOptions.merge())
                .addOnSuccessListener {
                }.addOnFailureListener { exception ->
                Log.d("버그", "StudentRepository에서 소개업로드실패: ${exception.message}")
            }
        }
    }
    //학생 상세 소개 로컬에 저장
    fun storeStudentIntroduce(introduce: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            studentDatastoreHelper.insertIntroduce(introduce)
        }
    }
    //파이어베이스에서 학생 등록 완료 처리 및 유저 상태 학생으로 변경
    fun registerStudentComplete()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val userInfo = hashMapOf(
                "student" to "true",
                "status" to "student"
            )
            db.collection("users").document(uid).set(userInfo, SetOptions.merge())
                .addOnFailureListener {
                    Log.d("버그", "StudentRepository에서 선생님 등록 완료 실패")
                }
        }
    }
    //로컬에서 학생 등록 완료 처리 및 유저 상태 학생으로 변경
    fun registerStudentCompleteLocal() {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertStudent("true")
            userDataStoreHelper.insertStatus("student")
        }
    }
    //학생 정보 변경(파이어베이스)
    fun modifyStudentInfo(fixInfo : String, fixName : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            var fixInfoArticle: String? = null
            when (fixInfo) {
                "닉네임" -> fixInfoArticle = "nickname"
                "한줄소개" -> fixInfoArticle = "des"
                "수업 방식" -> fixInfoArticle = "way"
                "자기소개" -> fixInfoArticle = "introduce"

            }
            val data = hashMapOf(
                fixInfoArticle to fixName
            )
            db.collection("students").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                Log.d("버그", "StudentRepository에서 정보 변경 못하는 중 : ${exception.message}")
            }
        }
    }
    //학생 정보 변경(로컬)
    fun modifyStudentInfoLocal(fixInfo : String, fixName : String)
    {
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
    //학생 배열 정보 변경(파이어베이스)
    fun modifyStudentInfoArray(fixInfo : String, fixName : List<String>)
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
            db.collection("students").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "StudentRepository에서 정보 변경 못하는 중 : ${exception.message}")
                }
        }
    }
    //학생 배열 정보 변경(로컬)
    fun modifyStudentInfoArrayLocal(fixInfo: String,fixName: String)
    {
        when(fixInfo) {
            "지역" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertAvailableLocal(fixName)
                }
            "과목" ->
                CoroutineScope(Dispatchers.IO).launch {
                    studentDatastoreHelper.insertSubject(fixName)
                }

        }
    }
    //파이어베이스에서 학생 정보 받아오기
    fun getStudentData(uid: String): LiveData<StudentInfo> {
        val studentInfo = MutableLiveData<StudentInfo?>()
        // 코루틴 스코프 내에서 비동기 작업을 수행
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = db.collection("students").document(uid).get().await()
                var info = documentSnapshot.toObject(StudentInfo::class.java)
                if (info != null) {
                    while (info!!.nickname == null || info.des == null || info.bornYear == null || info.gender == null
                        || info.way == null|| info.availableLocal == null|| info.subject == null|| info.introduce == null
                    ) {
                        try {
                            val documentSnapshot = db.collection("students").document(uid).get().await()
                            info = documentSnapshot.toObject(StudentInfo::class.java)
                        }
                        catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Log.d("버그", "StudentRepository에서 학생 정보 못 받아 오는중: ${e.message}")
                                studentInfo.value = null
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    studentInfo.value = info
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("버그", "StudentRepository에서 학생 정보 못 받아 오는중: ${e.message}")
                    studentInfo.value = null
                }
            }
        }

        return studentInfo as LiveData<StudentInfo>
    }
    //파이어베이스에서 학생 프사 불러와서 로컬에 넣기
    fun getStudentProfileImage(uid: String,context: Context)
        {
        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("students/$uid")
        val localFile = File(context?.filesDir, "$uid" + "student.jpg")
        profileImageRef.getFile(localFile).addOnFailureListener { exception ->
                Log.d("버그","StudentRepository에서 사진 못 받아오는 중 ")
            }
        }
        //학생 로컬데이터 조회
        val studentDes: Flow<String> get() = studentDatastoreHelper.getDes
        val studentBornYear: Flow<String> get() = studentDatastoreHelper.getBornYear
        val studentGender: Flow<String> get() = studentDatastoreHelper.getGender
        val studentWay: Flow<String> get() = studentDatastoreHelper.getWay
        val studentLocal: Flow<String> get() = studentDatastoreHelper.getAvailableLocal
        val studentSubject: Flow<String> get() = studentDatastoreHelper.getSubject
        val studentIntroduce: Flow<String> get() = studentDatastoreHelper.getIntroduce
    }