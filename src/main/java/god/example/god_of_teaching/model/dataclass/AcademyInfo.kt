package god.example.god_of_teaching.model.dataclass
//학원 정보
data class AcademyInfo(
    val uid: String?=null,
    val nickname: String?=null,
    val des: String?=null,
    val classAge: String?=null,
    val localNumber: String?=null,
    val local: String?=null,
    val detailAddress: String?=null,
    val searchedLocal:List<String>?=null,
    val subject: List<String>?=null,
    val introduce: String?=null,
    val matchingNum: String?=null,
    val searchedAllow: Boolean?=null
)
