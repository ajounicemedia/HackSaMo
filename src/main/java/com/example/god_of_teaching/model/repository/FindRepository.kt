package com.example.god_of_teaching.model.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.example.god_of_teaching.model.pagingsource.AcademyPagingSource
import com.example.god_of_teaching.model.pagingsource.StudentPagingSource
import com.example.god_of_teaching.model.pagingsource.TeacherPagingSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

//찾기에서 사용되는 레포지토리
class FindRepository {
    val searchedAcademys: MutableLiveData<MutableList<AcademyInfo>> = MutableLiveData()
    val searchedTeachers: MutableLiveData<MutableList<TeacherInfo>> = MutableLiveData()
    val searchedStudents: MutableLiveData<MutableList<StudentInfo>> = MutableLiveData()
    val db = FirebaseFirestore.getInstance()

//학원찾기
fun getAcademiesPaged(selectedSubject: List<String>, selectedLocation: List<String>): Flow<PagingData<AcademyInfo>> {
    return Pager(
        config = PagingConfig(pageSize = 30)
    ) {
        AcademyPagingSource(db, selectedSubject, selectedLocation)
    }.flow
}
    //선생님 찾기
    fun getTeachersPaged(selectedSubject: List<String>, selectedLocation: List<String>,gender: String): Flow<PagingData<TeacherInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 30)
        ) {
            TeacherPagingSource(db, selectedSubject, selectedLocation,gender)
        }.flow
    }
    //학생 찾기
    fun getStudentsPaged(selectedSubject: List<String>, selectedLocation: List<String>,gender: String): Flow<PagingData<StudentInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 30)
        ) {
            StudentPagingSource(db, selectedSubject, selectedLocation,gender)
        }.flow
    }
}