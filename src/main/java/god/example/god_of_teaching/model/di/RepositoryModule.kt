package god.example.god_of_teaching.model.di


import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import god.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import god.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import god.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper
import god.example.god_of_teaching.model.repository.AcademyRepository
import god.example.god_of_teaching.model.repository.AcademyToStudentChatRepository
import god.example.god_of_teaching.model.repository.AuthRepository
import god.example.god_of_teaching.model.repository.ChatRepository
import god.example.god_of_teaching.model.repository.FindRepository
import god.example.god_of_teaching.model.repository.MyInfoRepository
import god.example.god_of_teaching.model.repository.StudentRepository
import god.example.god_of_teaching.model.repository.StudentToAcademyChatRepository
import god.example.god_of_teaching.model.repository.StudentToTeacherChatRepository
import god.example.god_of_teaching.model.repository.TeacherRepository
import god.example.god_of_teaching.model.repository.TeacherToStudentChatRepository
import god.example.god_of_teaching.model.repository.WishListRepository


//뷰모델에서 레포지토리를 편리하게 사용하기 위한 hilt
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {




    @Provides//인증 레포지토리
    fun authRepository(userDataStoreHelper: UserDataStoreHelper,teacherDataStoreHelper: TeacherDataStoreHelper
    ,academyDatastoreHelper: AcademyDatastoreHelper,studentDatastoreHelper: StudentDatastoreHelper
    ,sharedPreferences: SharedPreferences) : AuthRepository {
        return AuthRepository(userDataStoreHelper,teacherDataStoreHelper,academyDatastoreHelper,studentDatastoreHelper,sharedPreferences)
    }
    @Provides//선생님 레포지토리
    fun teacherRepository(
        teacherDataStoreHelper: TeacherDataStoreHelper,
        userDataStoreHelper: UserDataStoreHelper
    ): TeacherRepository {
        return TeacherRepository(teacherDataStoreHelper, userDataStoreHelper)
    }
    @Provides//학원 레포지토리
    fun academyRepository(userDataStoreHelper: UserDataStoreHelper,
                                 academyDatastoreHelper: AcademyDatastoreHelper
    ) : AcademyRepository {
        return AcademyRepository(userDataStoreHelper,academyDatastoreHelper)
    }
    @Provides//찾기 레포지토리
    fun findRepository() : FindRepository {
        return FindRepository()
    }
    @Provides//내 정보 레포지토리
    fun myInfoRepository(userDataStoreHelper: UserDataStoreHelper) : MyInfoRepository
    {
        return MyInfoRepository(userDataStoreHelper)
    }
    @Provides//학생 레포지토리
    fun studentRepository(studentDatastoreHelper: StudentDatastoreHelper,
                          userDataStoreHelper: UserDataStoreHelper
    ) : StudentRepository
    {
        return StudentRepository(studentDatastoreHelper,userDataStoreHelper)
    }
    @Provides//위시 리스트 레포지토리
    fun wishListRepository(userDataStoreHelper: UserDataStoreHelper) : WishListRepository
    {
        return WishListRepository(userDataStoreHelper)
    }
    @Provides//채팅 레포지토리
    fun chatRepository(userDataStoreHelper: UserDataStoreHelper, sharedPreferences: SharedPreferences) : ChatRepository
    {
        return ChatRepository(userDataStoreHelper,sharedPreferences)
    }
    @Provides//선생님 -> 학생 채팅 레포지토리
    fun studentToTeacherChatRepository(userDataStoreHelper: UserDataStoreHelper) : StudentToTeacherChatRepository
    {
        return StudentToTeacherChatRepository(userDataStoreHelper)
    }

    @Provides//학생 -> 선생님 채팅 레포지토리
    fun teacherToStudentChatRepository(userDataStoreHelper: UserDataStoreHelper) : TeacherToStudentChatRepository
    {
        return TeacherToStudentChatRepository(userDataStoreHelper)
    }
    @Provides//학생 -> 학원 채팅 레포지토리
    fun studentToAcademyChatRepository(userDataStoreHelper: UserDataStoreHelper) : StudentToAcademyChatRepository
    {
        return StudentToAcademyChatRepository(userDataStoreHelper)
    }
    @Provides//학원 -> 학생 채팅 레포지토리
    fun academyToStudentChatRepository(userDataStoreHelper: UserDataStoreHelper) : AcademyToStudentChatRepository
    {
        return academyToStudentChatRepository(userDataStoreHelper)
    }

}