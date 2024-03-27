package god.example.god_of_teaching.model.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import god.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import god.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import god.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import god.example.god_of_teaching.model.datastore.UserDataStoreHelper

//뷰모델에서 데이타스토어를 편리하게 사용하기 위한 hilt
@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
    @Provides//유저 데이타 스토어
    fun provideUserDataStoreHelper(application: Application): UserDataStoreHelper {
        return UserDataStoreHelper.getInstance(application)
    }

    @Provides//선생님 데이타 스토어
    fun provideTeacherDataStoreHelper(application: Application): TeacherDataStoreHelper {
        return TeacherDataStoreHelper.getInstance(application)
    }
    @Provides//학원 데이타 스토어
    fun provideAcademyDataStoreHelper(application: Application): AcademyDatastoreHelper {
        return AcademyDatastoreHelper.getInstance(application)
    }

    @Provides//학원 데이타 스토어
    fun provideStudentDataStoreHelper(application: Application): StudentDatastoreHelper {
        return StudentDatastoreHelper.getInstance(application)
    }
    @Provides//SharedPreferences
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    }
}