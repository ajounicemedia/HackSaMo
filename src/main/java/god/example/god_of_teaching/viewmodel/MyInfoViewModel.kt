package god.example.god_of_teaching.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.repository.MyInfoRepository
import javax.inject.Inject

//내정보레포지토리 호출 + 로컬에 데이터 삽입
@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val myInfoRepository : MyInfoRepository,
    application: Application) : AndroidViewModel(application) {

    //myInfoRepository - changeNicknameLocal 호출
    fun changeNicknameLocal(nickname: String) {
        myInfoRepository.changeNicknameLocal(nickname)
    }
    //myInfoRepository - changeNickname 호출
    private val _nicknameChangeCheck = MutableLiveData<Boolean?>()
    val nicknameChangeCheck: LiveData<Boolean?>
        get() = _nicknameChangeCheck
    fun changeNickname(nickname: String) {
        myInfoRepository.changeNickname(nickname)
        {
            _nicknameChangeCheck.value = it
        }
    }
    //무한 관찰 방지
    fun changeNickname()
    {
        _nicknameChangeCheck.value =null
    }
    //myInfoRepository - passwordCheck 호출
    private val _passwordCheck = MutableLiveData<Boolean?>(null)
    val passwordCheck: LiveData<Boolean?>
        get() = _passwordCheck
    fun passwordCheck(password: String){
        myInfoRepository.passwordCheck(password)
        {
            _passwordCheck.value = it
        }
    }
    //무한 관찰 반복 막기 위해서
    fun resetPasswordCheck() {
        _passwordCheck.value = null
    }
    //myInfoRepository - changePassword 호출
    private val _changePasswordCheck = MutableLiveData<Boolean>(null)
    val changePasswordCheck: LiveData<Boolean>
        get() = _changePasswordCheck
    fun changePassword(password: String) {
        myInfoRepository.changePassword(password)
        {
            _changePasswordCheck.value = it
        }
    }
    //무한 관찰 반복 막기 위해서
    fun resetChangePasswordCheck() {
        _passwordCheck.value = null
    }
    //myInfoRepository - changeUserStatus, changeUserStatusLocal 호출
    fun changeMyStatus(status: String)
    {

        myInfoRepository.changeUserStatus(status)
        myInfoRepository.changeUserStatusLocal(status)

    }
    //myInfoRepository에서 조회한 내 로컬데이터 조회
    val teacherCheck = myInfoRepository.teacherCheck.asLiveData()
    val academyCheck = myInfoRepository.academyCheck.asLiveData()
    val studentCheck = myInfoRepository.studentCheck.asLiveData()
    val status = myInfoRepository.status.asLiveData()
    val uid = myInfoRepository.uid.asLiveData()

}