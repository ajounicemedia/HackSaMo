package god.example.god_of_teaching.model.repository


import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

//내 정보 관리 레포지토리
class MyInfoRepository@Inject constructor(
    private val userDataStoreHelper : UserDataStoreHelper
) {
    private val db = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    //닉네임 변경
    fun changeNickname(nickname: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
                val uid = userDataStoreHelper.getUid.first()
                db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val defaultNickname = document.getString("nickname")
                            if (defaultNickname != null) {
                                if (defaultNickname == nickname) {//닉네임 중복 체크
                                    callback(false)
                                } else {
                                    val data = hashMapOf(
                                        "nickname" to nickname
                                    )
                                    if (uid != null) {
                                        db.collection("users").document(uid).set(data, SetOptions.merge())
                                    }
                                    callback(true)
                                }
                            } else {
                                Log.d("버그", "MyInfoRepository의 changeNickname에서 파이어 스토어 접근 못 하는 중(닉네임 값이 없음)")
                                callback(false)
                            }
                        } else {
                            Log.d("버그", "MyInfoRepository의 changeNickname에서 파이어 스토어 접근 못 하는 중(uid 값이 없음)")
                            callback(false)
                        }
                    }
                    .addOnFailureListener {
                        Log.d("버그", "MyInfoRepository의 changeNickname에서 파이어 스토어 접근 못 하는 중")
                        callback(false)
                    }
            }
    }
    //로컬에 닉네임 변경한거 반영
    fun changeNicknameLocal(nickname: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertStatus(nickname)
        }
    }
    //비밀번호 확인
    fun passwordCheck(password: String, callback: (Boolean) -> Unit) {
        user?.reauthenticate(EmailAuthProvider.getCredential(user.email!!, password))
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }
    //비밀번호 변경
    fun changePassword(password: String, callback: (Boolean) -> Unit) {
        user?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true)
            } else {
                Log.d("버그", "MyInfoRepository에서 비밀번호 변경 못하는 중")
                callback(false)
            }
        }
    }
    //상태 변경
    fun changeUserStatus(status:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "status" to status
            )
            db.collection("users").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "MyInfoRepository에서 상태 변경 못하는 중 : ${exception.message}")
                }
        }
    }
    //로컬에 상태 변경한거 반영
    fun changeUserStatusLocal(status:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertStatus(status)
        }

    }
    //내 정보 로컬데이터 조회

    val teacherCheck: Flow<String> get() = userDataStoreHelper.getTeacher
    val academyCheck: Flow<String> get() = userDataStoreHelper.getAcademy
    val studentCheck: Flow<String> get() = userDataStoreHelper.getStudent
    val status: Flow<String> get() = userDataStoreHelper.getStatus
    val uid : Flow<String> get() = userDataStoreHelper.getUid

}


