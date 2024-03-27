package god.example.god_of_teaching.model.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import god.example.god_of_teaching.model.dataclass.AcademyInfo
import god.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

//학원 데이터 관리 레포지토리
class AcademyRepository(private val userDataStoreHelper: UserDataStoreHelper,
                        private val academyDatastore: AcademyDatastoreHelper) {
    private val db = Firebase.firestore
    //닉네임, 한줄 소개, 수업 대상 파이어 베이스로 업로드
    fun uploadAcademyInfo(des: String, classAge: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val nickname = userDataStoreHelper.getNickname.first()
            val data = hashMapOf("nickname" to nickname, "des" to des, "classAge" to classAge)
            db.collection("academies").document(uid).set(data).addOnFailureListener{ exception ->
                Log.d("버그", "AcademyRepository에서 uid, 닉네임, 한줄 소개, 수업 대상 ${exception.message}")
            }
        }
    }
    //닉네임, 한줄 소개, 수업 대상 로컬에 저장
    fun storeAcademyInfo(des: String, classAge: String)
    {
        val data = hashMapOf("des" to des,"class_Age" to classAge)
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertData(data)
        }
    }
    //파이어스토리지에 학원 프로필 사진 업로드
    fun uploadAcademyProfileImage(uri: Uri)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val storageReference = Firebase.storage.reference.child("academies/$uid")
            val uploadTask = storageReference.putFile(uri)
            uploadTask.addOnFailureListener {
                Log.d("버그", "AcademyRepository에서 프사 업로드 실패: ${it.message}", it)
            }
        }
    }
    //로컬에 학원 프로필 사진 저장
    fun storeAcademyProfileImage(uri: Uri, context: Context)
    {
        CoroutineScope(Dispatchers.IO).launch {
                val uid = userDataStoreHelper.getUid.first()
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri!!)
                val filename = uid+"academy.jpg"
                val file = File(context?.filesDir, filename)
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
            }
    }
    //파이어스토리지에 학원 등록증 사진 업로드
    fun uploadAcademyAuthImage(uri: Uri)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val storageReference = Firebase.storage.reference.child("academyAuth/$uid")
            val uploadTask = storageReference.putFile(uri)
            uploadTask
                .addOnFailureListener {
                    Log.d("버그", "AcademyRepository에서 학원등록증 사진 업로드 실패: ${it.message}", it)
                }
        }
    }
    //학원 주소 파이어베이스에 업로드
    fun uploadAcademyLocal(localNum : String, local : String, detailAddress : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "localNumber" to localNum,
                "local" to local,
                "detailAddress" to detailAddress
            )
            db.collection("academies").document(uid).set(data, SetOptions.merge())
                .addOnSuccessListener {
                }.addOnFailureListener { exception ->
                Log.d("버그", "AcademyRepository에서 지역 업로드 실패: ${exception.message}")
            }
        }
    }
    //학원 주소 로컬에 저장
    fun storeAcademyLocal(localNum : String, local : String, detailAddress : String)
    {
        val data = hashMapOf("local_Number" to localNum, "local" to local, "detail_Address" to  detailAddress)
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertData(data)
        }
    }
    //검색될 학원 주소 업로드
    fun uploadSearchedAddressAcademy(searchedLocal: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "searchedLocal" to searchedLocal
            )
            db.collection("academies").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                Log.d("버그", "AcademyRepository에서 검색 될 지역 업로드 실패: ${exception.message}")
            }
        }
    }
    //검색될 학원 주소 로컬에 저장
    fun storeSearchedAddressAcademy(searchedLocal: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertSearchedLocal(searchedLocal)
        }
    }
    //학원 수업 과목 파이어 베이스에 업로드
    fun uploadAcademySubject(subjectList: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "subject" to subjectList
            )
            db.collection("academies").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "AcademyRepository에서 과목 업로드 실패: ${exception.message}")
                }
        }
    }
    //학원 수업 과목 로컬에 저장
    fun storeAcademySubject(subjectList: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertSubject(subjectList)
        }
    }
    //학원 상세 소개 파이어 베이스에 업로드
    fun uploadAcademyIntroduce(introduce: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val data = hashMapOf(
                "introduce" to introduce
            )
            db.collection("academies").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "AcademyRepository에서 소개 업로드 못하고 있음: ${exception.message}")
                }
        }
    }
    //학원 상세 소개 로컬에 저장
    fun storeAcademyIntroduce(introduce: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            academyDatastore.insertIntroduce(introduce)
        }
    }
    //학원 등록 완료 처리
    fun completeAcademyRegister()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            val userInfo = hashMapOf(
                "academy" to "true",
                "status" to "academy"
            )
            db.collection("users").document(uid).set(userInfo, SetOptions.merge())
                .addOnFailureListener {
                    Log.w("버그", "AcademyRepository에서 학원 등록 완료 실패")
                }
        }
    }
    //로컬에서 학원 등록 완료 처리
    fun completeAcademyRegisterLocal()
    {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStoreHelper.insertStatus("academy")
            userDataStoreHelper.insertAcademy("true")
        }
    }
    //파이어베이스에서 학원 정보 변경
    fun modifyAcademyInfo(fixInfo: String, fixName: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            var fixInfoArticle : String? = null
            when(fixInfo)
            {
                "한줄소개" ->  fixInfoArticle ="des"
                "학원소개"->fixInfoArticle = "introduce"
                "검색 지역"->fixInfoArticle = "searchedLocal"
            }
            val data = hashMapOf(
                fixInfoArticle to fixName
            )
            db.collection("academies").document(uid).set(data, SetOptions.merge()).addOnFailureListener { exception ->
                Log.d("버그", "StudentRepository에서 정보 변경 못하는 중 : ${exception.message}")
            }
        }
    }
    //로컬에서 학원 정보 변경
    fun modifyAcademyInfoLocal(fixInfo: String, fixName: String)
    {
        when(fixInfo) {
            "한줄소개" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertDes(fixName)
                }
            "학원 소개" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertIntroduce(fixName)
                }
        }
    }
    //파이어베이스에서 학원 정보 변경(배열)
    fun modifyAcademyInfoArray(fixInfo: String, fixName: List<String>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val uid = userDataStoreHelper.getUid.first()
            var fixInfoArticle: String? = null
            when (fixInfo) {
                "과목" -> fixInfoArticle = "subject"
                "수업 대상" -> fixInfoArticle = "classAge"
                "검색 지역" -> fixInfoArticle = "searchedLocal"
            }
            val data = hashMapOf(
                fixInfoArticle to fixName
            )
            db.collection("academies").document(uid).set(data, SetOptions.merge())
                .addOnFailureListener { exception ->
                    Log.d("버그", "academyRepository에서 정보 변경 못하는 중 : ${exception.message}")
                }
        }
    }
    //로컬에서 학원 정보 변경(배열)
    fun modifyAcademyInfoArrayLocal(fixInfo: String, fixName: String)
    {
        when(fixInfo) {
            "과목" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertSubject(fixName)
                }
            "수업 대상" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertClassAge(fixName)
                }
            "검색 지역" ->
                CoroutineScope(Dispatchers.IO).launch {
                    academyDatastore.insertSearchedLocal(fixName)
                }
        }
    }

    //파이어베이스에서 학원 정보 받아오기
    fun getAcademyData(uid: String): LiveData<AcademyInfo> {
        val academyInfo = MutableLiveData<AcademyInfo?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = db.collection("academies").document(uid).get().await()
                var info = documentSnapshot.toObject(AcademyInfo::class.java)
                if (info != null) {
                    while (info!!.nickname == null || info.des == null || info.classAge == null || info.localNumber == null
                        || info.local == null|| info.detailAddress == null|| info.searchedLocal == null|| info.subject == null
                        || info.introduce == null
                    ) {
                        try {
                            val documentSnapshot = db.collection("academies").document(uid).get().await()
                            info = documentSnapshot.toObject(AcademyInfo::class.java)
                        }
                        catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Log.d("버그", "AcademyRepository에서 학원 정보 못 받아 오는중: ${e.message}")
                                academyInfo.value = null
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    academyInfo.value = info
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("버그", "AcademyRepository에서 학원 정보 못 받아 오는중: ${e.message}")
                    academyInfo.value = null
                }
            }
        }
        return academyInfo as LiveData<AcademyInfo>
    }
    //학원 프로필 사진 받아오기
    fun getAcademyProfileImage(uid: String,context: Context)
    {

        val storageReference = FirebaseStorage.getInstance().reference
        val profileImageRef = storageReference.child("academies/$uid")
        val localFile = File(context?.filesDir, "$uid" + "academy.jpg")

        profileImageRef.getFile(localFile)
            .addOnFailureListener { exception ->
                Log.d("버그","AcademyRepository에서 사진 못 받아오는 중 ")
            }
    }

    //학원 로컬데이터 조회
    val academyDes : Flow<String> get()= academyDatastore.getDes
    val academyClassAge : Flow<String> get() = academyDatastore.getClassAge
    val academyLocal : Flow<String> get() = academyDatastore.getLocal
    val academyDetailAddress : Flow<String> get() = academyDatastore.getDetailAddress
    val academySubject : Flow<String> get() = academyDatastore.getSubject
    val academyIntroduce : Flow<String> get() = academyDatastore.getIntroduce
    val academyAvailableLocal : Flow<String> get() = academyDatastore.getSearchedLocal


}