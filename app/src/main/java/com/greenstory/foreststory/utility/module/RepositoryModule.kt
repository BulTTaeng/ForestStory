package com.greenstory.foreststory.utility.module

import com.greenstory.foreststory.repository.MainRepository
import com.greenstory.foreststory.repository.audio.AudioRepository
import com.greenstory.foreststory.repository.contents.CommentatorRepository
import com.greenstory.foreststory.repository.contents.MountainRepository
import com.greenstory.foreststory.repository.login.LoginRepository
import com.greenstory.foreststory.repository.contents.setting.SettingRepository
import com.greenstory.foreststory.repository.contents.setting.VerifyRepository
import com.greenstory.foreststory.repository.contents.setting.add.AddAudioRepository
import com.greenstory.foreststory.repository.contents.setting.add.AddProgramRepository
import com.greenstory.foreststory.repository.contents.setting.delete.DeleteProgramRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule() {

    @Provides
    fun provideMainRepo() : MainRepository{
        return MainRepository()
    }

    @Provides
    fun provideLoginRepo() : LoginRepository{
        return LoginRepository()
    }

    @Provides
    fun provideSettingRepo() : SettingRepository{
        return SettingRepository()
    }

    @Provides
    fun provideAudioRepo() : AudioRepository{
        return AudioRepository()
    }

    @Provides
    fun provideMountainRepo() : MountainRepository{
        return MountainRepository()
    }

    @Provides
    fun provideCommentatorRepo() : CommentatorRepository{
        return CommentatorRepository()
    }

    @Provides
    fun provideAddProgramRepo() : AddProgramRepository{
        return AddProgramRepository()
    }

    @Provides
    fun provideAddAudioRepo() : AddAudioRepository {
        return AddAudioRepository()
    }

    @Provides
    fun provideVerifyRepo() : VerifyRepository {
        return VerifyRepository()
    }

    @Provides
    fun provideDeleteProgramRepo() : DeleteProgramRepository {
        return DeleteProgramRepository()
    }
}