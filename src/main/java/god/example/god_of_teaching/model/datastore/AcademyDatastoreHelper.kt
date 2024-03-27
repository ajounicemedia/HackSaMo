package god.example.god_of_teaching.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
//나의 학원 정보를 내부 로컬에 저장하는 클래스
class AcademyDatastoreHelper(context : Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("academy_pref")
    private val mDataStore: DataStore<Preferences> = context.dataStore

    private val nickname = stringPreferencesKey("nickname")
    private val des = stringPreferencesKey("des")
    private val classAge =stringPreferencesKey("class_Age")
    private val localNumber = stringPreferencesKey("local_Number")
    private val local = stringPreferencesKey("local")
    private val detailAddress = stringPreferencesKey("detail_Address")
    private val subject = stringPreferencesKey("subject")
    private val introduce = stringPreferencesKey("introduce")
    private val searchedLocal = stringPreferencesKey("searched_Local")

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

    suspend fun insertSearchedLocal(searchedLocal: String) {
        mDataStore.edit { settings ->
            settings[this.searchedLocal] = searchedLocal
        }
    }
    suspend fun insertNickname(nickname: String) {
        mDataStore.edit { settings ->
            settings[this.nickname] = nickname
        }
    }
    suspend fun insertDes(des: String) {
        mDataStore.edit { settings ->
            settings[this.des] = des
        }
    }
    suspend fun insertClassAge(classAge: String)
    {
        mDataStore.edit { settings ->
            settings[this.classAge] = classAge
        }
    }
    suspend fun insertLocalNumber(localNumber: String) {
        mDataStore.edit { settings ->
            settings[this.localNumber] = localNumber
        }
    }
    suspend fun insertLocal(local: String) {
        mDataStore.edit { settings ->
            settings[this.local] = local
        }
    }
    suspend fun insertDetailAddress(detailAddress: String) {
        mDataStore.edit { settings ->
            settings[this.detailAddress] = detailAddress
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
    val getNickname: Flow<String> = mDataStore.data.map {
        val nickname = it[nickname] ?: "default"
        nickname
    }
    val getDes: Flow<String> = mDataStore.data.map {
        val des = it[des] ?: "default"
        des
    }
    val getClassAge: Flow<String> = mDataStore.data.map {
        val classAge = it[classAge] ?: "default"
        classAge
    }
    val getLocalNumber: Flow<String> = mDataStore.data.map {
        val localNumber = it[localNumber] ?: "default"
        localNumber
    }
    val getLocal: Flow<String> = mDataStore.data.map {
        val local= it[local] ?: "default"
        local
    }
    val getDetailAddress: Flow<String> = mDataStore.data.map {
        val detailAddress = it[detailAddress] ?: "default"
        detailAddress
    }
    val getSubject: Flow<String> = mDataStore.data.map {
        val subject = it[subject] ?: "default"
        subject
    }
    val getIntroduce: Flow<String> = mDataStore.data.map {
        val introduce = it[introduce] ?: "default"
        introduce
    }
    val getSearchedLocal: Flow<String> = mDataStore.data.map {
        val searchedLocal = it[searchedLocal] ?: "default"
        searchedLocal
    }
    //모든 데이터 삭제
    suspend fun clearDataStore() {
        mDataStore.edit { it.clear() }
    }
    //유일성 보장을 위해서 선언함
    companion object {
        @Volatile
        private var INSTANCE: AcademyDatastoreHelper? = null

        fun getInstance(context: Context): AcademyDatastoreHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = AcademyDatastoreHelper(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}