package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.repository.TeacherRepository
import javax.inject.Inject


//선생님 레포지토리 호출 + 로컬에 데이터 삽입
@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    application: Application
) : AndroidViewModel(application) {


    //teacherRepository - uploadTeacherInfo, storeTeacherInfo 호출
    fun uploadTeacherInfo(des: String, bornYear: String,
                          gender: String, campus: String, campusLocal: String,
                          major: String, status: String, way: String)
    {
        teacherRepository.uploadTeacherInfo(des, bornYear, gender,campus, campusLocal, major, status, way)
        teacherRepository.storeTeacherInfo(des, bornYear, gender,campus, campusLocal, major, status, way)
    }

    //teacherRepository - uploadTeacherAvailableLocal, storeTeacherAvailableLocal 호출
    fun uploadTeacherAvailableLocal(localList: List<String>)
    {
        teacherRepository.uploadTeacherAvailableLocal(localList)
        teacherRepository.storeTeacherAvailableLocal(localList)
    }
    //academyRepository - uploadAcademyAuthImage 호출
    fun uploadTeacherAuthImage(uri: Uri)
    {
        teacherRepository.uploadTeacherAuthImage(uri)
    }
    //teacherRepository - uploadTeacherSubject, storeTeacherSubject 호출
    fun uploadTeacherSubject(subjectList: List<String>)
    {
        teacherRepository.uploadTeacherSubject(subjectList)
        teacherRepository.storeTeacherSubject(subjectList)
    }

    //teacherRepository - uploadTeacherIntroduce, storeTeacherIntroduce 호출
    fun uploadTeacherIntroduce(introduce : String)
    {
        teacherRepository.uploadTeacherIntroduce(introduce)
        teacherRepository.storeTeacherIntroduce(introduce)
    }
    //teacherRepository - uploadTeacherProfileImage, storeTeacherProfileImage 호출
    fun uploadTeacherProfileImage(uri: Uri, context: Context)
    {
        teacherRepository.uploadTeacherProfileImage(uri)
        teacherRepository.storeTeacherProfileImage(uri,context)
    }

    //teacherRepository - registerCompleteTeacher, registerCompleteTeacherLocal 호출
    fun registerCompleteTeacher()
    {
        teacherRepository.registerCompleteTeacher()
        teacherRepository.registerCompleteTeacherLocal()
    }

    //teacherRepository - modifyTeacherInfo, modifyTeacherInfoLocal 호출
    fun modifyTeacherInfo(fixInfo: String, fixName: String)
    {
        teacherRepository.modifyTeacherInfo(fixInfo, fixName)
        teacherRepository.modifyTeacherInfoLocal(fixInfo, fixName)
    }
    //teacherRepository - modifyTeacherInfoArray, modifyTeacherInfoArrayLocal 호출
    fun modifyTeacherInfoArray(fixInfo: String, fixName: List<String>)
    {
        val combinedString = fixName.joinToString(", ")
        teacherRepository.modifyTeacherInfoArray(fixInfo, fixName)
        teacherRepository.modifyTeacherInfoArrayLocal(fixInfo,combinedString)
    }

    //teacherRepository에서 내 선생님 로컬 데이터 조회
    val readDes =  teacherRepository.teacherDes.asLiveData()
    val readBornYear =  teacherRepository.teacherBornYear.asLiveData()
    val readGender =  teacherRepository.teacherGender.asLiveData()
    val readCampus =  teacherRepository.teacherCampus.asLiveData()
    val readCampusLocal =  teacherRepository.teacherCampusLocal.asLiveData()
    val readStatus =  teacherRepository.teacherStatus.asLiveData()
    val readLocal =  teacherRepository.teacherLocal.asLiveData()
    val readSubject =  teacherRepository.teacherSubject.asLiveData()
    val readIntroduce =  teacherRepository.teacherIntroduce.asLiveData()
    val readWay =  teacherRepository.teacherWay.asLiveData()

}

