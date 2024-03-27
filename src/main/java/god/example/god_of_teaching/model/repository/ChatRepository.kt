package god.example.god_of_teaching.model.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import god.example.god_of_teaching.model.dataclass.ReportReasonInfo
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

//채팅에서 차단, 알림 끄기, 신고하기를 위한 레포지토리
class ChatRepository@Inject constructor(private val userDataStoreHelper : UserDataStoreHelper,
                                        private val sharedPreferences: SharedPreferences) {
    private val db = Firebase.firestore

    //블랙리스트 추가
    fun addBlackList(otherName : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val blacklistData = hashMapOf("blacklist" to otherName)
            db.collection("users").document(uid).collection("blacklistUid").document(otherName)
                .set(blacklistData)
                .addOnFailureListener { exception ->
                    Log.d("버그", "ChatRepository - 블랙리스트 추가 못하는 중 : ${exception.message} + $uid")
                }
        }
    }
    //로컬에 블랙리스트 추가
    fun addBlackListLocal(otherName: String) {
        val blackList = sharedPreferences.getStringSet("blacklist", HashSet()) ?: HashSet()
        blackList.add(otherName)
        val editor = sharedPreferences.edit()
        editor.putStringSet("blacklist", blackList)
        editor.apply()
        Log.d("SharedPrefs", "Added to blacklist: $otherName")
        Log.d("SharedPrefs", "Current blacklist: $blackList")
    }
    //블랙리스트 삭제
    fun removeBlackList(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            db.collection("users").document(myUid).collection("blacklistUid").document(otherUid)
                .delete()
                .addOnFailureListener { exception ->
                    Log.d("버그", "ChatRepository - 블랙리스트 삭제 못하는 중 : ${exception.message} + $myUid")
                }
        }
    }
    //로컬에 블랙리스트 삭제
    fun removeBlackListLocal(otherName: String)
    {
        val blackList = sharedPreferences.getStringSet("blacklist", HashSet()) ?: return
        if (blackList.contains(otherName)) {
            blackList.remove(otherName)
            val editor = sharedPreferences.edit()
            editor.putStringSet("blacklist", blackList)
            editor.apply()
        }
    }
    //알람 차단 목록 추가(파이어베이스)
    fun addBlackListNotify(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val blacklistData = hashMapOf("blacklistNotify" to otherUid)
            db.collection("users").document(myUid).collection("blacklistNotify").document(otherUid)
                .set(blacklistData)
                .addOnFailureListener { exception ->
                    Log.d("버그", "ChatRepository - 알람 차단 목록 추가 못하는 중 : ${exception.message} + $myUid")
                }
        }
    }
    //로컬에 알람 차단 목록 추가
    fun addBlackListNotifyLocal(otherUid : String)
    {
        val blackNotification = sharedPreferences.getStringSet("blockNotification", HashSet()) ?: HashSet()
        blackNotification.add(otherUid)
        val editor = sharedPreferences.edit()
        editor.putStringSet("blockNotification", blackNotification)
        editor.apply()
        Log.d("SharedPrefs", "Added to NotificationBlocK: $otherUid")
        Log.d("SharedPrefs", "Current NotificationBlocK: $blackNotification")
    }
    //알람 차단 목록 삭제(파이어베이스)
    fun removeBlackListNotify(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            db.collection("users").document(myUid).collection("blacklistNotify").document(otherUid)
                .delete()
                .addOnFailureListener { exception ->
                    Log.d("버그", "ChatRepository - 알람 차단 목록 삭제 못하는 중 : ${exception.message} + $myUid")
                }
        }
    }
    //로컬에서 알람 차단 목록 삭제
    fun removeBlackListNotifyLocal(otherUid: String) {
        val blackList = sharedPreferences.getStringSet("blockNotification", HashSet()) ?: return
        if (blackList.contains(otherUid)) {
            blackList.remove(otherUid)
            val editor = sharedPreferences.edit()
            editor.putStringSet("blockNotification", blackList)
            editor.apply()
        }
    }
    //차단 유저 이유 업로드(파이어베이스)
    fun uploadReportReason(otherUid : String, reportReason: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val reportData = ReportReasonInfo(myUid, otherUid, reportReason)
            db.collection("reports").document(otherUid).collection("reportReason")
                .document(myUid).set(reportData)
                .addOnSuccessListener {}
                .addOnFailureListener { e ->
                    Log.d("버그", "ChatRepository에서 차단 이유 업로드 못하는 중: ", e)
                }
        }
    }
    //신고 유저 uid 업로드 (캐시 삭제 대비해서)
    fun uploadReportUserUid(otherUid : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val myUid = userDataStoreHelper.getUid.first()
            val reportUserUid = hashMapOf("reportedUid" to otherUid)
            db.collection("users").document(myUid).collection("reportedUid").document(otherUid)
                .set(reportUserUid)
                .addOnFailureListener { exception ->
                    Log.d("버그", "ChatRepository - 신고 목록 추가 못하는 중 : ${exception.message} + $myUid")
                }
        }
    }
    //블랙리스트 추가 됐는지 확인(로컬)
    fun checkBlackList(otherName: String): Boolean {
        val blackList = sharedPreferences.getStringSet("blacklist", HashSet())
        return blackList?.contains(otherName) ?: false
    }
    //알람 차단 됐는지 확인(로컬)
    fun checkBlackNotify(otherUid: String): Boolean {
        val blackList = sharedPreferences.getStringSet("blockNotification", HashSet())
        return blackList?.contains(otherUid) ?: false
    }
    //신고 했는지 안 했는지 확인(로컬)
    fun checkReport(otherUid: String): Boolean {
        val blackList = sharedPreferences.getStringSet("reportList", HashSet())
        return blackList?.contains(otherUid) ?: false
    }
    //블랙리스트 초기화(로컬)
    fun clearBlacklist() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("blacklist", HashSet<String>()) // 새로운 빈 HashSet으로 초기화
        editor.apply()
    }
    //알림 차단 목록 초기화(로컬)
    fun clearBlackNotification() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("blockNotification", HashSet<String>()) // 새로운 빈 HashSet으로 초기화
        editor.apply()
    }
    //상대 유저 프로필 사진 다운로드
    fun getOtherProfileImage(type: String,otherUid: String) : LiveData<String>
    {
        val otherUri =  MutableLiveData<String>()
        val storageReference = Firebase.storage.reference.child("$type/${otherUid}")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            if(uri!=null)
            {
                otherUri.value = uri.toString()
            }
            else
            {
                otherUri.value="null"
            }
        }.addOnFailureListener {
            otherUri.value="null"//실패 종류 별로 분류
            Log.d("버그", "ChatRepository - 사진 uid 조회 못하는 중")
        }
        return otherUri
    }
    // SharedPreferences에서 블랙리스트 가져오기
    fun getBlacklist(): List<String>? {
        return sharedPreferences.getStringSet("blacklist", emptySet())?.toList() ?: emptyList()
    }

}