package com.example.god_of_teaching.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WishListRepository()  {

    private val db = Firebase.firestore
    //학원 위시리스트 추가 삭제
    fun uplodeAcademyWishList(academyUid: String,uid : String)
    {
        val academyData = hashMapOf("academyUid" to academyUid)
        db.collection("users").document(uid).collection("wishlist").document("academy")
            .collection("items").document(academyUid)
            .set(academyData)
            .addOnSuccessListener {
                Log.d("", "WishListRepository - 학원을 위시리스트에 추가 성공, 문서 ID")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "WishListRepository - 학원을 위시리스트에 추가 못하는 중 : ${exception.message} + $uid")

            }
    }
    //삭제
    fun deleteAcademyWishListByField(uid: String, academyUid: String) {
        db.collection("users").document(uid)
            .collection("wishlist").document("academy")
            .collection("items")
            .document(academyUid)
            .delete()
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "WishListRepository - 학원 위시리스트에서 제거 못하는 중: ${exception.message}")
            }
    }

    //학생 위시리스트 추가 삭제
    fun uplodeStudentWishList(studentUid: String,uid : String)
    {
        val studentData = hashMapOf("studentUid" to studentUid)
        db.collection("users").document(uid).
        collection("wishlist").document("student")
            .collection("items")
            .document(studentUid)
            .set(studentData)
            .addOnSuccessListener { documentReference ->
                Log.d("", "WishListRepository - 학생을 위시리스트에 추가 성공, 문서 ID")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "WishListRepository - 학생을 위시리스트에 추가 못하는 중 : ${exception.message} + $uid")

            }
    }
    //삭제
    fun deleteStudentWishListByField(uid: String, studentUid: String) {
        db.collection("users").document(uid)
            .collection("wishlist").document("student")
            .collection("items")
            .document(studentUid)
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->
                Log.d("버그", "WishListRepository - 학셍 위시리스트에서 제거 못하는 중: ${exception.message}")
            }
    }

    //선생님 위시리스트 추가 삭제
    fun uplodeTeacherWishList(teacherUid: String,uid : String)
    {
        val teacherData = hashMapOf("teacherUid" to teacherUid)
        db.collection("users").document(uid).collection("wishlist").document("teacher")
            .collection("items").document(teacherUid)
            .set(teacherData)
            .addOnSuccessListener {
                Log.d("", "WishListRepository - 선생을 위시리스트에 추가 성공, 문서 ID:")
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "WishListRepository - 선생을 위시리스트에 추가 못하는 중 : ${exception.message} + $uid")

            }
    }
    //삭제
    fun deleteTeacherWishListByField(uid: String, teacherUid: String) {
        db.collection("users").document(uid)
            .collection("wishlist").document("teacher")
            .collection("items")
            .document(teacherUid)
            .delete()
            .addOnSuccessListener { querySnapshot ->
            }
            .addOnFailureListener { exception ->
                Log.d("버그", "WishListRepository - 학셍 위시리스트에서 제거 못하는 중: ${exception.message}")
            }
    }
    //내 학원 위시리스트 불러오기
    fun loadAcademyWishList(uid: String): MutableLiveData<List<AcademyInfo>>
    {
        //변경감지 코드
        val myAcademyWishList = MutableLiveData<List<AcademyInfo>>()

        db.collection("users").document(uid)
            .collection("wishlist").document("academy")
            .collection("items")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("버그", "위시리스트 레포지토리에서 학원 목록 감지 실패", e)
                    myAcademyWishList.postValue(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val academyUids = snapshot.documents.mapNotNull { it.getString("academyUid") }

                    if (academyUids.isNotEmpty()) {
                        db.collection("academys").whereIn("uid", academyUids)
                            .get()
                            .addOnSuccessListener { academyDocuments ->
                                val academyList =
                                    academyDocuments.mapNotNull { it.toObject(AcademyInfo::class.java) }
                                myAcademyWishList.postValue(academyList)
                            }
                            .addOnFailureListener {
                                Log.d("버그", "위리시트르 레포지토리에서 학원 정보 가져오기 실패")
                                myAcademyWishList.postValue(emptyList())
                            }
                    } else {
                        myAcademyWishList.postValue(emptyList())
                    }
                } else {
                    myAcademyWishList.postValue(emptyList())
                }
            }

        return myAcademyWishList

    }
    //내 선생님 위시리스트 불러오기
    fun loadTeacherWishList(uid: String): MutableLiveData<List<TeacherInfo>> {
        //전체 업로드
        //변경감지
        val myTeacherWishList = MutableLiveData<List<TeacherInfo>>()

        db.collection("users").document(uid)
            .collection("wishlist").document("teacher")
            .collection("items")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("버그", "위시리스트 레포지토리에서 학생 목록 감지 실패", e)
                    myTeacherWishList.postValue(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val teacherUids = snapshot.documents.mapNotNull { it.getString("teacherUid") }

                    if (teacherUids.isNotEmpty()) {
                        db.collection("teachers").whereIn("uid", teacherUids)
                            .get()
                            .addOnSuccessListener { teacherDocuments ->
                                val teacherList =
                                    teacherDocuments.mapNotNull { it.toObject(TeacherInfo::class.java) }
                                myTeacherWishList.postValue(teacherList)
                            }
                            .addOnFailureListener {
                                Log.d("버그", "위리시트르 레포지토리에서 선생님 정보 가져오기 실패")
                                myTeacherWishList.postValue(emptyList())
                            }
                    } else {
                        myTeacherWishList.postValue(emptyList())
                    }
                } else {
                    myTeacherWishList.postValue(emptyList())
                }
            }

        return myTeacherWishList
    }
    //내 학생 위시리스트 불러오기
    fun loadStudentWishList(uid: String): MutableLiveData<List<StudentInfo>> {
        //변경감지
        val myStudentWishList = MutableLiveData<List<StudentInfo>>()

        db.collection("users").document(uid)
            .collection("wishlist").document("student")
            .collection("items")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("버그", "위시리스트 레포지토리에서 학생 목록 감지 실패", e)
                    myStudentWishList.postValue(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val studentUids = snapshot.documents.mapNotNull { it.getString("studentUid") }

                    if (studentUids.isNotEmpty()) {
                        db.collection("students").whereIn("uid", studentUids)
                            .get()
                            .addOnSuccessListener { studentDocuments ->
                                val studentList =
                                    studentDocuments.mapNotNull { it.toObject(StudentInfo::class.java) }
                                myStudentWishList.postValue(studentList)
                            }
                            .addOnFailureListener {
                                Log.d("버그", "위리시트르 레포지토리에서 학생 정보 가져오기 실패")
                                myStudentWishList.postValue(emptyList())
                            }
                    } else {
                        myStudentWishList.postValue(emptyList())
                    }
                } else {
                    myStudentWishList.postValue(emptyList())
                }
            }

        return myStudentWishList
    }


}