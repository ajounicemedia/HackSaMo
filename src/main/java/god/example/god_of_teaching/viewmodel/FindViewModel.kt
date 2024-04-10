package god.example.god_of_teaching.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.dataclass.AcademyInfo
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.repository.FindRepository
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
    //FindFragment 에서 사용하기 위한 뷰모델 변수
    val selectedLocations: MutableLiveData<MutableList<String>?> = MutableLiveData(null)
    val selectedSubjects: MutableLiveData<MutableList<String>?> = MutableLiveData(null)
    val selectedGender: MutableLiveData<String> = MutableLiveData(null)
    var objectStudent = MutableLiveData<Boolean>(false)
    var objectAcademy = MutableLiveData<Boolean>(false)
    var objectTeacher = MutableLiveData<Boolean>(false)
    //Bundle 데이터 전달용
    var academyKey : MutableLiveData<AcademyInfo>? = MutableLiveData(null)
    var teacherKey : MutableLiveData<TeacherInfo>? = MutableLiveData(null)
    var studentKey : MutableLiveData<StudentInfo>? = MutableLiveData(null)


    //searchedAcademies - 관찰
    val searchedAcademies: MutableLiveData<MutableList<AcademyInfo>?> = MutableLiveData(null)
    init {
        findRepository.searchedAcademies.observeForever { academies ->
            searchedAcademies.postValue(academies)
        }
    }
    //searchedTeachers - 관찰
    val searchedTeachers:MutableLiveData<MutableList<TeacherInfo>?> = MutableLiveData(null)
    init {
        findRepository.searchedTeachers.observeForever { teachers ->
            searchedTeachers.postValue(teachers)
        }
    }
    //searchedStudents - 관찰
    val searchedStudents:MutableLiveData<MutableList<StudentInfo>?> = MutableLiveData(null)
    init {
        findRepository.searchedStudents.observeForever { students ->
            searchedStudents.postValue(students)
        }
    }
    //findRepository - getAcademiesPaged 호출
    private val _pagedAcademies = MutableStateFlow<PagingData<AcademyInfo>>(PagingData.empty())
    val pagedAcademies: StateFlow<PagingData<AcademyInfo>> = _pagedAcademies
    fun findAcademies(selectedSubject: List<String>, selectedLocation: List<String>) {
        viewModelScope.launch {
            findRepository.getAcademiesPaged(selectedSubject, selectedLocation)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _pagedAcademies.value = pagingData
                }
        }
    }
    //findRepository - getTeachersPaged 호출
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
    //findRepository - getStudentsPaged 호출
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