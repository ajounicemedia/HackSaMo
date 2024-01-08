package com.example.god_of_teaching.model.repository

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//비밀번호, 닉네임 변경하기 위한 레포지토리
class MyInfoRepository {
    private val db = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    //닉네임 변경
    fun changeNickname(nickname: String, uid: String, callback: (Boolean) -> Unit) {
        if (uid != null) {
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
                                    db.collection("users").document(uid)
                                        .set(data, SetOptions.merge())
                                }
                                callback(true)
                            }

                        } else {
                            Log.d(
                                "버그",
                                "MyInfoRepository의 changeNickname에서 파이어 스토어 접근 못 하는 중(닉네임 값이 없음)"
                            )
                            callback(false)
                        }
                    } else {
                        Log.d(
                            "버그",
                            "MyInfoRepository의 changeNickname에서 파이어 스토어 접근 못 하는 중(uid 값이 없음)"
                        )
                        callback(false)
                    }
                }
                .addOnFailureListener {
                    callback(false)
                    Log.d("버그", "MyInfoRepository의 changeNickname에서 파이어 스토어 접근 못 하는 중")
                }
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
    fun changeUserStatus(uid: String,status:String)
    {
        val data = hashMapOf(
            "status" to status
        )
        db.collection("users").document(uid).set(data, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {exception ->
            Log.d("버그", "MyInfoRepository에서 상태 변경 못하는 중 : ${exception.message}")
        }
    }



}


