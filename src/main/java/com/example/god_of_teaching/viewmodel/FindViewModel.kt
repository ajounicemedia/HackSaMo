package com.example.god_of_teaching.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.example.god_of_teaching.model.repository.FindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

//찾기 뷰모델
@HiltViewModel
class FindViewModel @Inject constructor
    (private val findRepository : FindRepository
)  : ViewModel(){
    //어떤거 검색할건지 체크
    val selectedLocations: MutableLiveData<MutableList<String>?> = MutableLiveData(null)
    val selectedSubjects: MutableLiveData<MutableList<String>?> = MutableLiveData(null)
    val selectedGender: MutableLiveData<String> = MutableLiveData(null)
    var objectStudent = MutableLiveData<Boolean>(false)
    var objectAcademy = MutableLiveData<Boolean>(false)
    var objectTeacher = MutableLiveData<Boolean>(false)
    //데이터 전달용
    var academyKey : MutableLiveData<AcademyInfo>? = MutableLiveData(null)
    var teacherKey : MutableLiveData<TeacherInfo>? = MutableLiveData(null)
    var studentKey : MutableLiveData<StudentInfo>? = MutableLiveData(null)


    val searchedAcademys: MutableLiveData<MutableList<AcademyInfo>?> = MutableLiveData(null)
    init {
        //학원 라이브 데이터 관찰
        findRepository.searchedAcademys.observeForever { academies ->
            searchedAcademys.postValue(academies)
        }
    }

    val searchedTeachers:MutableLiveData<MutableList<TeacherInfo>?> = MutableLiveData(null)
    init {
        //선생님 라이브 데이터 관찰
        findRepository.searchedTeachers.observeForever { teachers ->
            searchedTeachers.postValue(teachers)
        }
    }

    val searchedStudents:MutableLiveData<MutableList<StudentInfo>?> = MutableLiveData(null)
    init {
        //학생 라이브 데이터 관찰
        findRepository.searchedStudents.observeForever { students ->
            searchedStudents.postValue(students)
        }
    }
    private val _pagedAcademies = MutableStateFlow<PagingData<AcademyInfo>>(PagingData.empty())
    val pagedAcademies: StateFlow<PagingData<AcademyInfo>> = _pagedAcademies
    //학원 찾기
    fun findAcademys(selectedSubject: List<String>, selectedLocation: List<String>) {
        viewModelScope.launch {
            findRepository.getAcademiesPaged(selectedSubject, selectedLocation)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _pagedAcademies.value = pagingData
                }
        }
    }
    //선생님 찾기
    private val _pagedTeachers = MutableStateFlow<PagingData<TeacherInfo>>(PagingData.empty())
    val pagedTeachers: StateFlow<PagingData<TeacherInfo>> = _pagedTeachers
    fun findTeachers(selectedSubject: List<String>, selectedLocation: List<String>,gender: String) {
        viewModelScope.launch {
            findRepository.getTeachersPaged(selectedSubject, selectedLocation, gender)
                .cachedIn(viewModelScope)
                .collectLatest {  pagingData ->
                    _pagedTeachers.value =  pagingData
                }
        }
    }
    //학생 찾기
    private val _pagedStudents = MutableStateFlow<PagingData<StudentInfo>>(PagingData.empty())
    val pagedStudents: StateFlow<PagingData<StudentInfo>> = _pagedStudents
    fun findStudents(selectedSubject: List<String>, selectedLocation: List<String>,gender: String)
    {
        viewModelScope.launch {
            findRepository.getStudentsPaged(selectedSubject, selectedLocation, gender)
                .cachedIn(viewModelScope)
                .collectLatest {  pagingData ->
                    _pagedStudents.value =  pagingData
                }
        }
    }


}