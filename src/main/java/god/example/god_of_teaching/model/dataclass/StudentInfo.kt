package god.example.god_of_teaching.model.dataclass
//학생 정보
data class StudentInfo(
    val uid:String?=null,
    val nickname: String?=null,
    val des:String?=null,
    val bornYear:String?=null,
    val gender:String?=null,
    val way:String?=null,
    val availableLocal: List<String>?=null,
    val subject: List<String>?=null,
    val introduce: String?=null,
    val searchedAllow: Boolean?=null
)
