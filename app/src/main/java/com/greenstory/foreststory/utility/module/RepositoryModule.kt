package com.greenstory.foreststory.utility.module

import com.greenstory.foreststory.repository.MainRepository
import com.greenstory.foreststory.repository.audio.AudioRepository
import com.greenstory.foreststory.repository.login.LoginRepository
import com.greenstory.foreststory.repository.contents.setting.SettingRepository
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
}