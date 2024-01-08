package com.example.god_of_teaching.model.di


import com.example.god_of_teaching.model.repository.AcademyChatRepository
import com.example.god_of_teaching.model.repository.AcademyRepository
import com.example.god_of_teaching.model.repository.AuthRepository
import com.example.god_of_teaching.model.repository.ChatRepository
import com.example.god_of_teaching.model.repository.FindRepository
import com.example.god_of_teaching.model.repository.MyInfoRepository
import com.example.god_of_teaching.model.repository.StudentChatRepository
import com.example.god_of_teaching.model.repository.StudentRepository
import com.example.god_of_teaching.model.repository.TeacherChatRepository
import com.example.god_of_teaching.model.repository.TeacherRepository
import com.example.god_of_teaching.model.repository.WishListRepository
import com.google.api.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
//뷰모델에서 레포지토리를 편리하게 사용하기 위한 hilt
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {




    @Provides//인증 레포지토리
    fun provideAuthRepository() : AuthRepository {
        return AuthRepository()
    }
    @Provides//선생님 레포지토리
    fun provideTeacherRepository() : TeacherRepository{
        return TeacherRepository()
    }
    @Provides//선생님 레포지토리
    fun provideAcademyRepository() : AcademyRepository {
        return AcademyRepository()
    }
    @Provides//찾기 레포지토리
    fun provideFindRepository() : FindRepository {
        return FindRepository()
    }
    @Provides//내 정보 레포지토리
    fun myInfoRepository() : MyInfoRepository
    {
        return MyInfoRepository()
    }
    @Provides//학생 레포지토리
    fun studentRepository() : StudentRepository
    {
        return StudentRepository()
    }
    @Provides//위시 리스트 레포지토리
    fun wishListRepository() : WishListRepository
    {
        return WishListRepository()
    }
    @Provides//채팅 레포지토리
    fun chatRepository() : ChatRepository
    {
        return ChatRepository()
    }
    @Provides//선생님 채팅 레포지토리
    fun teacherChatRepository() : TeacherChatRepository
    {
        return TeacherChatRepository()
    }

    @Provides//학생 채팅 레포지토리
    fun studentChatRepository() : StudentChatRepository
    {
        return StudentChatRepository()
    }
    @Provides//학원 채팅 레포지토리
    fun academyChatRepository() : AcademyChatRepository
    {
        return AcademyChatRepository()
    }
}