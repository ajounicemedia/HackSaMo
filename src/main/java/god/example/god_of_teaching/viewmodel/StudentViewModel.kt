package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.repository.StudentRepository
import javax.inject.Inject

//학생 레포지토리 호출 + 로컬에 학생데이터 삽입
@HiltViewModel
class StudentViewModel@Inject constructor(
    private val studentRepository : StudentRepository,
    application: Application) : AndroidViewModel(application) {

    //studentRepository - getStudentData, getStudentProfileImage 호출
    var studentInfo: LiveData<StudentInfo>?=null

    fun getStudentData(uid: String,context: Context)
    {
        studentInfo = studentRepository.getStudentData(uid)
        studentRepository.getStudentProfileImage(uid,context)
    }
    //studentRepository - uploadStudentInfo, storeStudentInfo 호출
    fun uploadStudentInfo(des: String, bornYear: String, gender: String, way: String)
    {
        studentRepository.uploadStudentInfo(des, bornYear, gender, way)
        studentRepository.storeStudentInfo(des, bornYear, gender, way)
    }
    //studentRepository - uploadStudentProfileImage, storeStudentProfileImage 호출
    fun uploadStudentProfileImage(uri: Uri, context: Context)
    {
        studentRepository.uploadStudentProfileImage(uri)
        studentRepository.storeStudentProfileImage(uri, context)
    }
    //studentRepository - uploadStudentLocal, storeStudentLocal 호출
    fun uploadStudentAvailableLocal(localList: List<String>)
    {
        val combinedString = localList.joinToString(", ")
        studentRepository.uploadStudentLocal(localList)
        studentRepository.storeStudentLocal(combinedString)
    }
    //studentRepository - uploadStudentSubject, storeStudentSubject 호출
    fun uploadStudentSubject(subjectList: List<String>)
    {
        val combinedString = subjectList.joinToString(", ")
        studentRepository.uploadStudentSubject(subjectList)
        studentRepository.storeStudentSubject(combinedString)
    }
    //studentRepository - uploadStudentIntroduce, storeStudentIntroduce 호출
    fun uploadStudentIntroduce(introduce : String)
    {
        studentRepository.uploadStudentIntroduce(introduce)
        studentRepository.storeStudentIntroduce(introduce)
    }
    //studentRepository - registerStudentComplete, registerStudentCompleteLocal 호출
    fun registerStudentComplete()
    {
        studentRepository.registerStudentComplete()
        studentRepository.registerStudentCompleteLocal()
    }
    //studentRepository - modifyStudentInfo, modifyStudentInfoLocal 호출
    fun modifyStudentInfo(fixInfo: String, fixName: String)
    {
        studentRepository.modifyStudentInfo(fixInfo, fixName)
        studentRepository.modifyStudentInfoLocal(fixInfo, fixName)
    }
    //studentRepository - modifyStudentInfoArray, modifyStudentInfoArrayLocal 호출
    fun modifyStudentInfoArray(fixInfo: String, fixName: List<String>)
    {
        val combinedString = fixName.joinToString(", ")
        studentRepository.modifyStudentInfoArray(fixInfo, fixName)
        studentRepository.modifyStudentInfoArrayLocal(fixInfo, combinedString)
    }
    //studentRepository에서 내 로컬 학생 데이터 조회
    val readDes = studentRepository.studentDes.asLiveData()
    val readBornYear = studentRepository.studentBornYear.asLiveData()
    val readGender = studentRepository.studentGender.asLiveData()
    val readWay = studentRepository.studentWay.asLiveData()
    val readLocal = studentRepository.studentLocal.asLiveData()
    val readSubject = studentRepository.studentSubject.asLiveData()
    val readIntroduce = studentRepository.studentIntroduce.asLiveData()


}