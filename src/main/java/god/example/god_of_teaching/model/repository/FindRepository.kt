package god.example.god_of_teaching.model.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import god.example.god_of_teaching.model.dataclass.AcademyInfo
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.pagingsource.AcademyPagingSource
import god.example.god_of_teaching.model.pagingsource.StudentPagingSource
import god.example.god_of_teaching.model.pagingsource.TeacherPagingSource
import kotlinx.coroutines.flow.Flow

//찾기에서 사용되는 레포지토리
class FindRepository {
    val searchedAcademies: MutableLiveData<MutableList<AcademyInfo>> = MutableLiveData()
    val searchedTeachers: MutableLiveData<MutableList<TeacherInfo>> = MutableLiveData()
    val searchedStudents: MutableLiveData<MutableList<StudentInfo>> = MutableLiveData()
    val db = FirebaseFirestore.getInstance()

    //AcademyPagingSource 호출
    fun getAcademiesPaged(selectedSubject: List<String>, selectedLocation: List<String>): Flow<PagingData<AcademyInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 1)
        ) {
            AcademyPagingSource(db, selectedSubject, selectedLocation)
        }.flow
    }
    //TeacherPagingSource 호출
    fun getTeachersPaged(selectedSubject: List<String>, selectedLocation: List<String>,gender: String): Flow<PagingData<TeacherInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 1)
        ) {
            TeacherPagingSource(db, selectedSubject, selectedLocation,gender)
        }.flow
    }
    //StudentPagingSource 호출
    fun getStudentsPaged(selectedSubject: List<String>, selectedLocation: List<String>,gender: String): Flow<PagingData<StudentInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 1)
        ) {
            StudentPagingSource(db, selectedSubject, selectedLocation,gender)
        }.flow
    }
}