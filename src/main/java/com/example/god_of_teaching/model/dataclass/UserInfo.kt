package com.example.god_of_teaching.model.dataclass
//유저 정보
data class UserInfo(
        val email: String?=null,
        val userUid: String?=null,
        val nickname: String?=null,
        val fcmToken: String?=null,
        val teacher: Boolean?=null,
        val academy: Boolean?=null,
        val student: Boolean?=null,
        val status: String?=null

)