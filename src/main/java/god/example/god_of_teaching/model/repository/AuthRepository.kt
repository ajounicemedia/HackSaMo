package god.example.god_of_teaching.model.repository


import android.content.Context
import android.content.SharedPreferences

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.FirebaseStorage
import god.example.god_of_teaching.R
import god.example.god_of_teaching.model.dataclass.AcademyInfo
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.dataclass.UserInfo
import god.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import god.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import god.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject


//회원가입, 로그인을 위한 레포지토리
class AuthRepository@Inject constructor(private val userDataStoreHelper: UserDataStoreHelper,
                                        private val teacherDataStoreHelper: TeacherDataStoreHelper,
                                        private val academyDatastoreHelper: AcademyDatastoreHelper,
                                        private val studentDatastoreHelper: StudentDatastoreHelper,
                                        private val sharedPreferences: SharedPreferences) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val db = Firebase.firestore
    private lateinit var uid : String
    //서버 키 로컬에 삽입
    fun getServerKey(context: Context)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertServerKey(getAccessToken(context))
        }
    }

    //서버 키 발급
    private fun getAccessToken(context: Context): String {
        val assetManager = context.assets
        val keyStream: InputStream = assetManager.open(context.getString(R.string.json_key))
        val credentials = GoogleCredentials.fromStream(keyStream)
            .createScoped(listOf(context.getString(R.string.firebase_messaging_key)))
        credentials.refreshIfExpired()
        return credentials.accessToken.tokenValue

    }

    //닉네임중복체크
    fun nicknameCheck(nickname: String,callback: (Boolean) -> Unit)
    {
        db.collection("users").whereEqualTo("nickname", nickname).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("버그", "AuthRepository - Firestore에서 닉네임 중복 검사 실패", exception)
                callback(false)
            }
    }
    //이메일중복체크
    fun sameEmailCheck(email: String, callback: (Boolean) -> Unit)
    {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isNotUsed = task.result?.signInMethods?.isEmpty() == true
                    if (isNotUsed) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            }
    }
    //로그인
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.user != null) {
                    val user = task.result!!.user!!
                    uid = user.uid // 여기서 Null이 아닌 경우에만 user의 uid를 가져옴
                    Firebase.messaging.token.addOnCompleteListener { fcmToken ->
                        val userInfo = hashMapOf(
                            "fcmToken" to fcmToken.result
                        )
                        if (uid != null) {
                            db.collection("users").document(uid).set(userInfo, SetOptions.merge())
                                .addOnFailureListener {
                                    Log.w("버그", "AuthRepository에서 회원가입 실패", task.exception)
                                }
                        }
                        callback(true, null.toString())
                    }.addOnFailureListener {
                        Log.d("버그", "AuthRepository login에서 토근 발급 실패", task.exception)
                    }

                } else {
                    var errorMessage = "로그인에 실패했습니다. 잠시 후 다시 시도해주세요."
                    if (task.exception is FirebaseAuthException) {
                        val errorCode = (task.exception as FirebaseAuthException).errorCode
                        errorMessage = when (errorCode) {
                            "ERROR_INVALID_EMAIL" -> "유효하지 않은 이메일 형식입니다."
                            "ERROR_WRONG_PASSWORD" -> "비밀번호가 일치하지 않습니다."
                            "ERROR_USER_NOT_FOUND" -> "존재하지 않는 이메일입니다."
                            "ERROR_NETWORK_REQUEST_FAILED" -> "인터넷 연결을 확인해주세요."
                            else -> errorMessage
                        }
                        Log.d("로그인 관찰", "실패: $errorCode")
                    }

                    callback(false, errorMessage)
                }
            }

    }
    //회원가입
    fun join(email: String, password: String,  nickname:String,callback: (Boolean) -> Unit) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            uid = task.result?.user?.uid.toString()


                            Firebase.messaging.token.addOnCompleteListener { fcmToken ->
                                val userInfo = UserInfo(
                                    email,
                                    uid,
                                    nickname,
                                    fcmToken.result,
                                    "false",
                                    "false",
                                    "false",
                                    "basic"
                                )

                                db.collection("users").document(uid).set(userInfo).addOnFailureListener {
                                            Log.w("버그", "AuthRepository에서 회원가입 실패", task.exception)
                                        }

                                callback(true)
                            }.addOnFailureListener {
                                Log.d("버그", "AuthRepository login에서 토근 발급 실패", task.exception)
                            }
                        } else {
                            Log.d("버그", "AuthRepository login에서 uid 접근 실패", task.exception)
                            callback(false)
                        }
                    }
    }
    //로컬에 닉네임, 이메일 넣기
    fun insertUserDataLocal(nickname: String,email: String)
    {
        val userDataMap = mapOf("nickname" to nickname,"email" to email)
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertData(userDataMap)
        }
    }

    //로그아웃
    fun logout( callback: (Boolean) -> Unit)
    {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        if(firebaseAuth.currentUser == null)
        {
            callback(true)
        }
        else
        {
            callback(false)
        }
    }
    //비밀번호 찾기
    fun findPassword(email: String, callback: (Pair<Boolean, String?>) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ResetPassword", "Email sent.")
                callback(Pair(true, null)) // 성공 메시지는 필요 없으므로 null
            } else {
                val exception = task.exception
                val errorMessage = when {
                    exception is FirebaseAuthInvalidUserException -> "존재하지 않는 사용자입니다."
                    exception is FirebaseAuthInvalidCredentialsException -> "이메일 형식이 잘못되었습니다."
                    exception is FirebaseNetworkException -> "인터넷 연결을 확인해주세요."
                    exception is FirebaseTooManyRequestsException -> "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 다시 시도해주세요."
                }
                Log.d("버그", "AuthRepository에서 비밀번호 찾기 중 오류: ${exception?.message}")
                callback(Pair(false, errorMessage))
            }
        }
    }

    //계정 탈퇴
    fun deleteAccount(deleteReason : String,callback: (Boolean) -> Unit)
    {
        val user = Firebase.auth.currentUser

        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val deleteReason = hashMapOf("deleteReason" to deleteReason)
                //신고 유저 uid 추가
                db.collection("deleteReason").add(deleteReason)
                    .addOnSuccessListener {
                        Log.d("", "AuthRepository - 탈퇴 이유 추가 성공")
                    }
                    .addOnFailureListener { exception ->
                        Log.d("버그", "AuthRepository - 탈퇴 목록 추가 못하는 중 : ${exception.message}")
                    }
                // 계정 삭제 성공
                callback(true)
            } else {
                // 계정 삭제 실패, 오류 처리
                Log.d("버그", "AuthRepository - 계정 삭제 실패: ${task.exception?.message}")
                callback(false)
            }
        }
    }
    //파이어베이스에서 유저정보 받아오기
    fun getUserData(uid: String,context: Context){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = db.collection("users").document(uid).get().await()
                var info = documentSnapshot.toObject(UserInfo::class.java)
                if (info != null) {
                    while (info!!.email == null || info.userUid == null || info.nickname == null || info.status == null
                        ||info.academy==null||info.teacher==null||info.student==null
                    ) {
                        try {
                            val documentSnapshot = db.collection("users").document(uid).get().await()
                            info = documentSnapshot.toObject(UserInfo::class.java)
                        }
                        catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Log.d("버그", "AuthRepository에서 유저 기본 정보 못 받아 오는중: ${e.message}")
                            }
                        }
                    }
                    val userDataMap = mapOf("uid" to info.userUid.toString(),"nickname" to info.nickname.toString(),"email" to info.email.toString(),
                        "teacher_Check" to info.teacher.toString(),"academy_Check" to info.academy.toString(),"student_Check" to info.student.toString(),
                    "status" to info.status.toString())
                    userDataStoreHelper.insertData(userDataMap)

                    if(info.teacher.toString()=="true")
                    {
                        getTeacherData(uid)
                        getTeacherProfileImage(uid,context)
                    }
                    if(info.academy.toString()=="true")
                    {
                        getAcademyData(uid)
                        getAcademyProfileImage(uid,context)
                    }
                    if(info.student.toString()=="true")
                    {
                        getStudentData(uid)
                        getStudentProfileImage(uid,context)
                    }
                }

            } catch (e: Exception) {
                    Log.d("버그", "AuthRepository에서 유저 기본 정보 못 받아 오는중: ${e.message}")
            }
        }
    }
    //파이어베이스에서 선생님 정보 불러오기
    private fun getTeacherData(uid: String){
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
                                Log.d("버그", "AuthRepository에서 선생님 정보 못 받아 오는중: ${e.message}")
                            }
                        }
                    }
                    val combinedLocal = info.availableLocal!!.joinToString(", ")
                    val combinedSubject = info.subject!!.joinToString(", ")
                    val teacherDataMap = mapOf("nickname" to info.nickname.toString(),"des" to info.des.toString(),"bornYear" to info.bornYear.toString(),"campus" to info.campus.toString(),
                        "campusLocal" to info.campusLocal.toString(),"major" to info.major.toString(),"status" to info.status.toString(),"way" to info.way.toString(),"available_Local" to combinedLocal,
                        "subject" to combinedSubject,"introduce" to info.introduce.toString(),"gender" to info.gender.toString())
                    teacherDataStoreHelper.insertData(teacherDataMap)
                }
            } catch (e: Exception) {
                    Log.d("버그", "AuthRepository에서 선생님 정보 못 받아 오는중: ${e.message}")
            }
        }
    }
    //파이어베이스에서 선생님 프사 불러와서 로컬에 넣기
    private fun getTeacherProfileImage(uid: String,context: Context) {

        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("teachers/$uid")
        val localFile = File(context.filesDir, uid + "teacher.jpg")
        Log.d("3213213216",localFile.toString())
        profileImageRef.getFile(localFile).addOnFailureListener { exception ->
            Log.d("버그", "AuthRepository에서 사진 못 받아오는 중 $exception")
        }
    }
    //파이어베이스에서 학원 정보 받아오기
    private fun getAcademyData(uid: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = db.collection("academies").document(uid).get().await()
                var info = documentSnapshot.toObject(AcademyInfo::class.java)
                if (info != null) {
                    while (info!!.nickname == null || info.des == null || info.classAge == null || info.localNumber == null
                        || info.local == null|| info.detailAddress == null|| info.searchedLocal == null|| info.subject == null
                        || info.introduce == null
                    ) {
                        try {
                            val documentSnapshot = db.collection("academies").document(uid).get().await()
                            info = documentSnapshot.toObject(AcademyInfo::class.java)
                        }
                        catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Log.d("버그", "AuthRepository에서 학원 정보 못 받아 오는중: ${e.message}")
                            }
                        }
                    }

                    val combinedLocal = info.searchedLocal!!.joinToString(", ")
                    val combinedSubject = info.subject!!.joinToString(", ")
                    val academyDataMap = mapOf("nickname" to info.nickname.toString(),"des" to info.des.toString(),"class_Age" to info.classAge.toString(),"localNumber" to info.localNumber.toString(),
                        "local" to info.local.toString(),"detail_Address" to info.detailAddress.toString(),"subject" to combinedSubject,"introduce" to info.introduce.toString(),"searched_Local" to combinedLocal)
                   academyDatastoreHelper.insertData(academyDataMap)
                }

            } catch (e: Exception) {
                    Log.d("버그", "AuthRepository에서 학원 정보 못 받아 오는중: ${e.message}")

            }
        }
    }
    //학원 프로필 사진 받아오기
    private fun getAcademyProfileImage(uid: String,context: Context)
    {

        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("academies/$uid")
        val localFile = File(context?.filesDir, uid + "academy.jpg")

        profileImageRef.getFile(localFile)
            .addOnFailureListener { exception ->
                Log.d("버그","AuthRepository에서 사진 못 받아오는 중 $exception ")
            }
    }
    //파이어베이스에서 학생 정보 받아오기
    private fun getStudentData(uid: String) {
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
                                Log.d("버그", "AuthRepository에서 학생 정보 못 받아 오는중: ${e.message}")
                            }
                        }
                    }
                    val combinedLocal = info.availableLocal!!.joinToString(", ")
                    val combinedSubject = info.subject!!.joinToString(", ")
                    val studentDataMap = mapOf("nickname" to info.nickname.toString(),"des" to info.des.toString(),"bornYear" to info.bornYear.toString(),"way" to info.way.toString(),
                        "available_Local" to combinedLocal,"subject" to combinedSubject,"introduce" to info.introduce.toString(),"gender" to info.gender.toString())
                    studentDatastoreHelper.insertData(studentDataMap)
                }

            } catch (e: Exception) {
                    Log.d("버그", "AuthRepository에서 학생 정보 못 받아 오는중: ${e.message}")
            }
        }

    }
    //파이어베이스에서 학생 프사 불러와서 로컬에 넣기
    private fun getStudentProfileImage(uid: String,context: Context)
    {
        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("students/$uid")
        val localFile = File(context?.filesDir, uid + "student.jpg")
        profileImageRef.getFile(localFile).addOnFailureListener { exception ->
            Log.d("버그","AuthRepository에서 사진 못 받아오는 중 $exception ")
        }
    }
    //블랙리스트 받아오기
    fun getBlacklist(uid: String){

        val myBlackList = mutableListOf<String>()
        val localBlackList = sharedPreferences.getStringSet("blacklist", HashSet()) ?: HashSet()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = db.collection("users").document(uid)
                    .collection("blacklistUid").get().await()

                if (querySnapshot.documents.isEmpty()) {


                } else {
                    val blacklist = querySnapshot.documents.mapNotNull { document ->
                        document.getString("blacklist")
                    }

                    myBlackList.addAll(blacklist)

                    val editor = sharedPreferences.edit()
                    editor.putStringSet("blacklist", HashSet<String>())
                    localBlackList.addAll(myBlackList)
                    editor.putStringSet("blacklist", localBlackList)
                    editor.apply()



                }
            } catch (e: Exception) {
                    Log.d("버그", "AuthRepository - 블랙리스트 가져오는 중 오류 발생: ${e.message}")
            }

        }


    }
    //알람 차딘리스트 가져오기
    fun getBlackNotify(uid: String) {

        val myBlackNotifyList= mutableListOf<String>()
        val localBlackNotification = sharedPreferences.getStringSet("blockNotification", HashSet()) ?: HashSet()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = db.collection("users").document(uid)
                    .collection("blacklistNotify").get().await()

                if (querySnapshot.documents.isEmpty()) {

                } else {
                    val blackNotifylist = querySnapshot.documents.mapNotNull { document ->
                        document.getString("blacklistNotify")
                    }

                    myBlackNotifyList.addAll(blackNotifylist)

                    val editor = sharedPreferences.edit()
                    editor.putStringSet("blockNotification", HashSet<String>())
                    localBlackNotification.addAll(myBlackNotifyList)
                    editor.putStringSet("blockNotification", localBlackNotification)
                    editor.apply()



                }
            } catch (e: Exception) {
                    Log.d("버그", "AuthRepository - 알림 차단리스트 가져오는 중 오류 발생: ${e.message}")
            }
        }


    }
    //
    fun getReportList(uid: String) {

        val myReportList= mutableListOf<String>()
        val localReportList = sharedPreferences.getStringSet("reportList", HashSet()) ?: HashSet()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = db.collection("users").document(uid)
                    .collection("reportedUid").get().await()

                if (querySnapshot.documents.isEmpty()) {

                } else {
                    val reportList = querySnapshot.documents.mapNotNull { document ->
                        document.getString("reportedUid")
                    }

                    myReportList.addAll(reportList)

                    val editor = sharedPreferences.edit()
                    editor.putStringSet("reportList", HashSet<String>())
                    localReportList.addAll(myReportList)
                    editor.putStringSet("reportList", localReportList)
                    editor.apply()

                }
            } catch (e: Exception) {
                    Log.d("버그", "AuthRepository - 차단 목록 가져오는 중 오류 발생: ${e.message}")
            }
        }


    }

    val nickname : Flow<String> get() = userDataStoreHelper.getNickname
    val email : Flow<String> get() = userDataStoreHelper.getEmail
}

