package com.example.god_of_teaching.view.nav.chat

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Nickname

class PreferenceUtils(private val sharedPreferences: SharedPreferences) {
    // 블랙리스트에 추가
    fun addBlackList(nickname: String) {
        val blackList = sharedPreferences.getStringSet("blacklist", HashSet()) ?: HashSet()
        blackList.add(nickname)
        val editor = sharedPreferences.edit()
        editor.putStringSet("blacklist", blackList)
        editor.apply()
    }

    // 블랙리스트에서 제거
    fun removeBlackList(nickname: String) {
        val blackList = sharedPreferences.getStringSet("blacklist", HashSet()) ?: return
        if (blackList.contains(nickname)) {
            blackList.remove(nickname)
            val editor = sharedPreferences.edit()
            editor.putStringSet("blacklist", blackList)
            editor.apply()
        }
    }

    // 블랙리스트 확인
    fun isBlackListed(nickname: String): Boolean {
        val blackList = sharedPreferences.getStringSet("blacklist", HashSet())
        return blackList?.contains(nickname) ?: false
    }
    //알람 차단 목록에 추가
    fun addNotificationBlocK(nickname: String) {
        val blackList = sharedPreferences.getStringSet("blockNotification", HashSet()) ?: HashSet()
        blackList.add(nickname)
        val editor = sharedPreferences.edit()
        editor.putStringSet("blockNotification", blackList)
        editor.apply()
    }
    //알람 차단 목록에서 삭제
    fun removeNotificationBlock(nickname: String) {
        val blackList = sharedPreferences.getStringSet("blockNotification", HashSet()) ?: return
        if (blackList.contains(nickname)) {
            blackList.remove(nickname)
            val editor = sharedPreferences.edit()
            editor.putStringSet("blockNotification", blackList)
            editor.apply()
        }
    }
    //알람 차단 됐는지 확인
    fun isBlackNotification(nickname: String): Boolean {
        val blackList = sharedPreferences.getStringSet("blockNotification", HashSet())
        return blackList?.contains(nickname) ?: false
    }

    //신고 목록 추가
    fun addReportList(nickname: String)
    {
        val blackList = sharedPreferences.getStringSet("reportList", HashSet()) ?: HashSet()
        blackList.add(nickname)
        val editor = sharedPreferences.edit()
        editor.putStringSet("reportList", blackList)
        editor.apply()
    }
    //신고 했는지 안 했는지 확인
    fun reportCheck(nickname: String): Boolean {
        val blackList = sharedPreferences.getStringSet("reportList", HashSet())
        return blackList?.contains(nickname) ?: false

    }

}