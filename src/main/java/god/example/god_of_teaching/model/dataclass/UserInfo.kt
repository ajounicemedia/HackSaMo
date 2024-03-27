package god.example.god_of_teaching.model.dataclass
//유저 정보
data class UserInfo(
        val email: String?=null,
        val userUid: String?=null,
        val nickname: String?=null,
        val fcmToken: String?=null,
        val teacher: String?=null,
        val academy: String?=null,
        val student: String?=null,
        val status: String?=null

)