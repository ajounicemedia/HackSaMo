package god.example.god_of_teaching.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
//나의 학생 정보를 내부 로컬에 저장하는 클래스
class StudentDatastoreHelper(context : Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("student_pref")
    private val mDataStore: DataStore<Preferences> = context.dataStore
    private val nickname = stringPreferencesKey("nickname")
    private val des = stringPreferencesKey("des")
    private val bornYear = stringPreferencesKey("bornYear")
    private val way = stringPreferencesKey("way")
    private val availableLocal = stringPreferencesKey("available_Local")
    private val subject = stringPreferencesKey("subject")
    private val introduce = stringPreferencesKey("introduce")
    private val gender = stringPreferencesKey("gender")

    suspend fun insertData(dataMap: Map<String, String>) {
        mDataStore.edit { settings ->
            dataMap.forEach { (key, value) ->
                settings[stringPreferencesKey(key)] = value
            }
        }
    }

    private fun getData(key: Preferences.Key<String>, defaultValue: String = "default"): Flow<String> {
        return mDataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    suspend fun insertGender(gender: String) {
        mDataStore.edit { settings ->
            settings[this.gender] = gender
        }
    }
    suspend fun insertNickname(nickname: String)
    {
        mDataStore.edit { settings ->
            settings[this.nickname] = nickname
        }
    }
    suspend fun insertDes(des: String)
    {
        mDataStore.edit { settings ->
            settings[this.des] = des
        }
    }
    suspend fun insertBornYear(bornYear: String) {
        mDataStore.edit { settings ->
            settings[this.bornYear] = bornYear
        }
    }
    suspend fun insertWay(way: String) {
        mDataStore.edit { settings ->
            settings[this.way] = way
        }
    }
    suspend fun insertAvailableLocal(availableLocal: String) {
        mDataStore.edit { settings ->
            settings[this.availableLocal] = availableLocal
        }
    }
    suspend fun insertSubject(subject: String) {
        mDataStore.edit { settings ->
            settings[this.subject] = subject
        }
    }
    suspend fun insertIntroduce(introduce: String) {
        mDataStore.edit { settings ->
            settings[this.introduce] = introduce
        }
    }
    val getGender: Flow<String> = mDataStore.data.map {
        val gender = it[gender] ?: "default"
        gender
    }
    val getNickname: Flow<String> = mDataStore.data.map {
        val nickname = it[nickname] ?: "default"
        nickname
    }
    val getDes: Flow<String> = mDataStore.data.map {
        val des = it[des] ?: "default"
        des
    }

    val getBornYear: Flow<String> = mDataStore.data.map {
        val bornYear = it[bornYear] ?: "default"
        bornYear
    }
    val getWay: Flow<String> = mDataStore.data.map {
        val way = it[way] ?: "default"
        way
    }
    val getAvailableLocal: Flow<String> = mDataStore.data.map {
        val availableLocal = it[availableLocal] ?: "default"
        availableLocal
    }
    val getSubject: Flow<String> = mDataStore.data.map {
        val subject = it[subject] ?: "default"
        subject
    }
    val getIntroduce: Flow<String> = mDataStore.data.map {
        val introduce = it[introduce] ?: "default"
        introduce
    }
    //모든 데이터 삭제
    suspend fun clearDataStore() {
        mDataStore.edit { it.clear() }
    }
    //유일성 보장을 위해서 선언함
    companion object {
        @Volatile
        private var INSTANCE: StudentDatastoreHelper? = null

        fun getInstance(context: Context): StudentDatastoreHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = StudentDatastoreHelper(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}