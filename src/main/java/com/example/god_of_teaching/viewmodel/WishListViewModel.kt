package com.example.god_of_teaching.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.example.god_of_teaching.model.repository.WishListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
//위시리스트 뷰모델
@HiltViewModel
class WishListViewModel @Inject constructor
    (private val wishListRepository: WishListRepository,
     private val userDataStoreHelper: UserDataStoreHelper,
     application: Application): AndroidViewModel(application)
{
    //학원 위시리스트 업로드 삭제
        fun uplodeAcademyWishList(academy : AcademyInfo) {
            CoroutineScope(Dispatchers.IO).launch {
                userDataStoreHelper.getUid.collect { uid ->
                    academy.uid?.let { wishListRepository.uplodeAcademyWishList(it, uid) }
                }
            }
        }
        fun deleteAcademyWishList(academy : AcademyInfo) {
            CoroutineScope(Dispatchers.IO).launch {
                userDataStoreHelper.getUid.collect { uid ->
                    academy.uid?.let { it1 ->
                        wishListRepository.deleteAcademyWishListByField(
                            uid,
                            it1
                        )
                    }
                }
            }
        }
    //학생 위시리스트 업로드 삭제
    fun uplodeStudentWishList(student : StudentInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                student.uid?.let { wishListRepository.uplodeStudentWishList(it, uid) }
            }
        }
    }
    fun deleteStudentWishList(student : StudentInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                student.uid?.let { it1 ->
                    wishListRepository.deleteStudentWishListByField(
                        uid,
                        it1
                    )
                }
            }
        }
    }
    //선생님 위시리스트 업로드 삭제
    fun uplodeTeacherWishList(teacher : TeacherInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                teacher.uid?.let { wishListRepository.uplodeTeacherWishList(it, uid) }
            }
        }
    }
    fun deleteTeacherWishList(teacher : TeacherInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                teacher.uid?.let { it1 ->
                    wishListRepository.deleteTeacherWishListByField(
                        uid,
                        it1
                    )
                }
            }
        }
    }
    // 내 학원 위시리스트 LiveData
    var myAcademyWishList = MutableLiveData<List<AcademyInfo>>()
    fun  loadAcademyWishList() {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                myAcademyWishList=wishListRepository.loadAcademyWishList(uid)
            }
        }
    }

    // 내 선생님 위시리스트 LiveData
    var myTeacherWishList = MutableLiveData<List<TeacherInfo>>()
    fun  loadTeacherWishList() {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                myTeacherWishList =wishListRepository.loadTeacherWishList(uid)
            }
        }
    }
    //내 학생 위시리스트 LiveData
    var myStudentWishList = MutableLiveData<List<StudentInfo>>()
    fun loadStudentWishList() {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.getUid.collect { uid ->
                myStudentWishList=wishListRepository.loadStudentWishList(uid)
            }
        }
    }
}