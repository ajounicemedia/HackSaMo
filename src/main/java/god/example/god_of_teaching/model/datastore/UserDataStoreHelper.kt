package god.example.god_of_teaching.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//필수적인 유저 정보를 내부 로컬에 저장하는 클래스
class UserDataStoreHelper(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_pref")
    private val mDataStore: DataStore<Preferences> = context.dataStore

    //private lateinit var subNickname : String
    private val uid = stringPreferencesKey("uid")
    private val nickname = stringPreferencesKey("nickname")
    private val email = stringPreferencesKey("email")
    private val gender = stringPreferencesKey("gender")
    private val teacher = stringPreferencesKey("teacher_Check")
    private val academy = stringPreferencesKey("academy_Check")
    private val student = stringPreferencesKey("student_Check")
    private val serverKey = stringPreferencesKey("server_Key")
    private val status =  stringPreferencesKey("status")

    suspend fun insertData(dataMap: Map<String, String>) {
        mDataStore.edit { settings ->
            dataMap.forEach { (key, value) ->
                settings[stringPreferencesKey(key)] = value
            }
        }
    }
    suspend fun insertUid(uid: String) {
        mDataStore.edit { settings ->
            settings[this.uid] = uid
        }
    }

    suspend fun insertNickname(nickname: String) {
        mDataStore.edit { settings ->
            settings[this.nickname] = nickname
        }
    }
    suspend fun insertEmail(email: String) {
        mDataStore.edit { settings ->
            settings[this.email] = email
        }
    }
    suspend fun insertTeacher(teacher: String) {
        mDataStore.edit { settings ->
            settings[this.teacher] = teacher
        }
    }
    suspend fun insertAcademy(academy: String) {
        mDataStore.edit { settings ->
            settings[this.academy] = academy
        }
    }
    suspend fun insertStudent(student: String) {
        mDataStore.edit { settings ->
            settings[this.student] = student
        }
    }
    suspend fun insertGender(gender: String) {
        mDataStore.edit { settings ->
            settings[this.gender] = gender
        }
    }
    suspend fun insertServerKey(serverKey: String) {
        mDataStore.edit { settings ->
            settings[this.serverKey] = serverKey
        }
    }

    suspend fun insertStatus(status: String) {
        mDataStore.edit { settings ->
            settings[this.status] = status
        }
    }
    val getNickname: Flow<String> = mDataStore.data.map {
        val nickname = it[nickname] ?: "default"
        nickname
    }

    val getEmail: Flow<String> = mDataStore.data.map {
        val email = it[email] ?: "default"
        email
    }
    val getUid: Flow<String> = mDataStore.data.map {
        val uid = it[uid] ?: "default"
        uid
    }
    val getStudent: Flow<String> = mDataStore.data.map {
        it[student] ?: "default"
    }

    val getTeacher: Flow<String> = mDataStore.data.map {
        it[teacher] ?: "default"
    }

    val getAcademy: Flow<String> = mDataStore.data.map {
        it[academy] ?: "default"
    }
    val getGender: Flow<String> = mDataStore.data.map {
        val gender = it[gender] ?: "default"
        gender
    }
    val getServerKey: Flow<String> = mDataStore.data.map {
        val serverKey = it[serverKey] ?: "default"
        serverKey
    }
    val getStatus: Flow<String> = mDataStore.data.map {
        val status = it[status] ?: "default"
        status
    }
    //모든 데이터 삭제
    suspend fun clearDataStore() {
        mDataStore.edit { it.clear() }
    }

    //유일성 보장을 위해서 선언함
    companion object {
        @Volatile
        private var INSTANCE: UserDataStoreHelper? = null

        fun getInstance(context: Context): UserDataStoreHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataStoreHelper(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}


