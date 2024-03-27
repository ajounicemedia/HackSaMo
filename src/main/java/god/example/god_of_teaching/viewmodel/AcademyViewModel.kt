package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.repository.AcademyRepository
import javax.inject.Inject


//학원 정보 로컬에 넣기 + 학원 레포지토리 호출
@HiltViewModel
class AcademyViewModel @Inject constructor
    (private val academyRepository : AcademyRepository,
     application: Application) : AndroidViewModel(application) {

    //academyRepository - uploadAcademyInfo, storeAcademyInfo 호출
    fun uploadAcademyInfo(des:String,classAge: List<String>) {
        val combinedString = classAge.joinToString(", ")
        academyRepository.uploadAcademyInfo(des, combinedString)
        academyRepository.storeAcademyInfo(des, combinedString)
    }
    //academyRepository - uploadAcademyProfileImage, storeAcademyProfileImage 호출
    fun uploadAcademyProfileImage(uri: Uri, context: Context) {
        academyRepository.uploadAcademyProfileImage(uri)
        academyRepository.storeAcademyProfileImage(uri, context)
    }
    //academyRepository - uploadAcademyAuthImage 호출
    fun uploadAcademyAuthImage(uri: Uri)
    {
        academyRepository.uploadAcademyAuthImage(uri)
    }
    //academyRepository - uploadAcademyLocal, storeAcademyLocal 호출
    fun uploadLocalData(localNum : String,local : String,detailAddress : String)
    {
        academyRepository.uploadAcademyLocal(localNum, local, detailAddress)
        academyRepository.storeAcademyLocal(localNum, local, detailAddress)
    }
    //academyRepository - uploadSearchedAddressAcademy, storeSearchedAddressAcademy 호출
    fun uploadSearchedLocalList(searchedLocal: List<String>)
    {
        val combinedString = searchedLocal.joinToString(", ")
        academyRepository.uploadSearchedAddressAcademy(searchedLocal)
        academyRepository.storeSearchedAddressAcademy(combinedString)
    }
    //academyRepository - uploadAcademySubject, storeAcademySubject 호출
    fun uploadSubject(subjectList: List<String>)
    {
        val combinedString = subjectList.joinToString(", ")
        academyRepository.uploadAcademySubject(subjectList)
        academyRepository.storeAcademySubject(combinedString)
    }
    //academyRepository - uploadAcademyIntroduce, storeAcademyIntroduce 호출
    fun uploadIntroduce(introduce : String)
    {
        academyRepository.uploadAcademyIntroduce(introduce)
        academyRepository.storeAcademyIntroduce(introduce)
    }
    //academyRepository - completeAcademyRegister, completeAcademyRegisterLocal 호출
    fun changeAcademyStatus()
    {
        academyRepository.completeAcademyRegister()
        academyRepository.completeAcademyRegisterLocal()
    }
    //academyRepository - modifyAcademyInfo, modifyAcademyInfoLocal 호출
    fun modifyAcademyInfo(fixInfo: String, fixName: String)
    {
        academyRepository.modifyAcademyInfo(fixInfo, fixName)
        academyRepository.modifyAcademyInfoLocal(fixInfo, fixName)
    }
    //academyRepository - modifyAcademyInfoArray, modifyAcademyInfoArrayLocal 호출
    fun modifyAcademyInfoArray(fixInfo: String, fixName: List<String>)
    {
        val combinedString = fixName.joinToString(", ")
        academyRepository.modifyAcademyInfoArray(fixInfo, fixName)
        academyRepository.modifyAcademyInfoArrayLocal(fixInfo, combinedString)

    }
    //academyRepository에서 내 학원 로컬 데이터 조회
    val readDes = academyRepository.academyDes.asLiveData()
    val readClassAge = academyRepository.academyClassAge.asLiveData()
    val readLocal = academyRepository.academyLocal.asLiveData()
    val readDetailAddress = academyRepository.academyDetailAddress.asLiveData()
    val readSubject = academyRepository.academySubject.asLiveData()
    val readIntroduce = academyRepository.academyIntroduce.asLiveData()
    val readAvailableLocal = academyRepository.academyAvailableLocal.asLiveData()

}