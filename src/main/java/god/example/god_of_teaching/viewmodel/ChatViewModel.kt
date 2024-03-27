package god.example.god_of_teaching.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.repository.ChatRepository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel@Inject constructor
    (private val chatRepository: ChatRepository,
     application: Application
): AndroidViewModel(application) {
    //chatRepository - addBlackList, addBlackListLocal 호출
    fun addBlackList(otherName : String)
    {
        chatRepository.addBlackList(otherName)
        chatRepository.addBlackListLocal(otherName)
    }
    //chatRepository - removeBlackList, removeBlackListLocal 호출
    fun removeBlackList(otherName : String)
    {
        chatRepository.removeBlackList(otherName)
        chatRepository.removeBlackListLocal(otherName)
    }
    //chatRepository - addBlackListNotify, addBlackListNotifyLocal 호출
    fun addBlackListNotify(otherUid : String)
    {
        chatRepository.addBlackListNotify(otherUid)
        chatRepository.addBlackListNotifyLocal(otherUid)
    }
    //chatRepository - removeBlackListNotify, removeBlackListNotifyLocal 호출
    fun removeBlackListNotify(otherUid : String)
    {
        chatRepository.removeBlackListNotify(otherUid)
        chatRepository.removeBlackListNotifyLocal(otherUid)
    }
    //chatRepository - uploadReportReason, uploadReportUserUid 호출
    fun uploadReportReason(reportedUid : String,reportReason: String)
    {
        chatRepository.uploadReportReason(reportedUid,reportReason)
        chatRepository.uploadReportUserUid(reportedUid)
    }
    //chatRepository - checkBlackList 호출
    fun checkBlackList(otherName : String) : Boolean
    {
        return chatRepository.checkBlackList(otherName)
    }
    //chatRepository - checkBlackNotify 호출
    fun checkBlackNotify(otherUid : String): Boolean
    {
        return chatRepository.checkBlackNotify(otherUid)
    }
    //chatRepository - checkReport 호출g
    fun checkReport(otherUid : String): Boolean
    {
        return chatRepository.checkReport(otherUid)
    }
    //chatRepository - getOtherProfileImage 호출
    var uri : LiveData<String>?=null
    fun getOtherProfileImage(type: String,otherUid: String)
    {
        uri = chatRepository.getOtherProfileImage(type,otherUid)
    }
    //chatRepository - getBlacklist 호출
    fun getBlacklist() : List<String>?
    {
        return chatRepository.getBlacklist()
    }
}