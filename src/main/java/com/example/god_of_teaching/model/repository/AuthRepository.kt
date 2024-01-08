package com.example.god_of_teaching.model.repository


import android.content.Context
import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging


//회원가입, 로그인을 위한 레포지토리
class AuthRepository() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val db = Firebase.firestore
    private lateinit var userUid : String

    //로그인
    fun login(
        email: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                userUid = task.result?.user?.uid.toString()
                if (task.isSuccessful&& Firebase.auth.currentUser!=null) {
                    Firebase.messaging.token.addOnCompleteListener { fcmToken ->
                        val userInfo = hashMapOf(

                            "fcmToken" to fcmToken.result
                        )
                        if (userUid != null) {
                            db.collection("users").document(userUid).set(userInfo, SetOptions.merge())
                                .addOnSuccessListener {
                                }
                                .addOnFailureListener {
                                    Log.w("버그", "AuthRepository에서 회원가입 실패", task.exception)
                                }
                        }

                        callback(true)
                    }.addOnFailureListener {
                        Log.d("버그", "AuthRepository login에서 토근 발급 실패", task.exception)
                    }


                } else {
                    Log.w("버그", "AuthRepository에서 로그인 실패", task.exception)
                    callback(false)
                }
            }
    }


    //회원가입
    fun join(email: String, password: String,  nickname:String,callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    userUid = task.result?.user?.uid.toString()


                    Firebase.messaging.token.addOnCompleteListener {fcmToken->


                    val userInfo = hashMapOf(
                        "email" to email,
                        "userUid" to userUid,
                        "nickname" to nickname,
                        "fcmToken" to fcmToken.result
                    )

                    if (userUid != null) {
                        db.collection("users").document(userUid).set(userInfo)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener {
                                Log.w("버그", "AuthRepository에서 회원가입 실패", task.exception)
                            }
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
        //로그아웃
        fun logout( callback: (Boolean) -> Unit)
        {
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()

            // 로그아웃 후 현재 사용자 확인
            if(firebaseAuth.currentUser == null) {
                // 로그아웃 성공
                callback(true)
            } else {
                // 로그아웃 실패
                callback(false)
            }


        }
    //비밀번호 찾기
    fun findPassword(email:String,callback: (Boolean) -> Unit)
    {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 이메일 전송 성공
                Log.d("ResetPassword", "Email sent.")
                callback(true)
            } else {
                // 에러 처리
                Log.d("버그", "비밀번호 못 찾는 중 Error: ${task.exception?.message}")
                callback(false)
            }
        }
    }

    //계정 탈퇴
    fun deleteAccount(callback: (Boolean) -> Unit)
    {
        val user = Firebase.auth.currentUser

        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 계정 삭제 성공
                callback(true)
            } else {
                // 계정 삭제 실패, 오류 처리
                Log.d("버그", "계정 삭제 실패: ${task.exception?.message}")
                callback(false)
            }
        }
    }


}

