package god.example.god_of_teaching.model.dataclass
//선생님 정보
data class TeacherInfo(
    val uid:String?=null,
    val nickname: String?=null,
    val des:String?=null,
    val bornYear:String?=null,
    val gender:String?=null,
    val campus:String?=null,
    val campusLocal:String?=null,
    val major:String?=null,
    val status:String?=null,
    val way:String?=null,
    val availableLocal: List<String>?=null,
    val subject: List<String>?=null,
    val introduce: String?=null,
    val matchingNum: String?=null,
    val searchedAllow: String?=null
)