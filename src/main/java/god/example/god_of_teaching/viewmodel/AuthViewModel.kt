package god.example.god_of_teaching.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.repository.AuthRepository
import javax.inject.Inject

//인증레포지토리 호출 + 라이브 데이터 관찰
@HiltViewModel
class AuthViewModel @Inject constructor (application: Application,
    private val authRepository: AuthRepository
) : AndroidViewModel(application){

    //authRepository - getServerKey 호출
    fun getServerKey(context: Context)
    {
        authRepository.getServerKey(context)
    }
    //authRepository - nicknameCheck 호출
    private val _nicknameSameCheck = MutableLiveData<Boolean?>()
    val  nicknameSameCheck : LiveData<Boolean?>
        get() = _nicknameSameCheck
    fun checkSameNickname(nickname: String)
    {
        authRepository.nicknameCheck(nickname)
        {
            _nicknameSameCheck.value = it
        }
    }
    fun resetCheckSameNickname()
    {
        _nicknameSameCheck.value = null
    }
    //authRepository - login 호출
    private val _loginCheck = MutableLiveData<Boolean>()
    val loginCheck: LiveData<Boolean>
        get() = _loginCheck
    fun login(email:String,password: String)
    {
        authRepository.login(email,password)
        {
            _loginCheck.value = it
        }
    }
    //authRepository - join 호출
    private val _joinCheck = MutableLiveData<Boolean>()
    val joinCheck: LiveData<Boolean>
        get() = _joinCheck
    fun join(email: String,password: String,nickname: String)
    {
        authRepository.join(email,password, nickname)
        {
            _joinCheck.value = it
        }
    }
    //authRepository - sameEmailCheck 호출
    private val _sameEmailCheck = MutableLiveData<Boolean>()
    val sameEmailCheck : LiveData<Boolean>
        get() = _sameEmailCheck
    fun sameEmailCheck(email: String)
    {
        authRepository.sameEmailCheck(email)
        {
            _sameEmailCheck.value = it
        }
    }
    //authRepository - logout 호출
    private val _logoutCheck = MutableLiveData<Boolean>()
    val logoutCheck: LiveData<Boolean>
        get() = _logoutCheck
    fun logout()
    {
        authRepository.logout() {
            _logoutCheck.value=it
        }
    }
    //authRepository - findPassword 호출
    private val _emailCheck = MutableLiveData<Boolean>()
    val  emailCheck: LiveData<Boolean>
        get() = _emailCheck
    fun sendEmail(email: String)
    {
        authRepository.findPassword(email)
        {
            _emailCheck.value=it
        }
    }
    //authRepository - deleteAccount 호출
    private val _accountDeleteCheck = MutableLiveData<Boolean>()
    val  accountDeleteCheck: LiveData<Boolean>
        get() = _accountDeleteCheck
    fun deleteAccount(deleteReason:String)
    {
        authRepository.deleteAccount(deleteReason)
        {
            _accountDeleteCheck.value = it
        }
    }
    //authRepository - getUserData 호출
    fun getUserInfo(uid: String,context: Context)
    {
        authRepository.getUserData(uid,context)
    }
    //authRepository - getBlacklist 호출
    fun getBlacklist(uid: String)
    {
        authRepository.getBlacklist(uid)
    }
    //authRepository - getBlackNotify 호출
    fun getBlackNotify(uid: String)
    {
       authRepository.getBlackNotify(uid)
    }
    //authRepository - getReportList
    fun getReportList(uid: String)
    {
        authRepository.getReportList(uid)
    }
    //authRepository - insertUserDataLocal 호출
    fun insertUserDataLocal(nickname: String, email: String)
    {
       authRepository.insertUserDataLocal(nickname, email)
    }


    //authRepository에서 내 닉네임, 이메일 조회
    val readNickname = authRepository.nickname.asLiveData()
    val readEmail = authRepository.email.asLiveData()


}


