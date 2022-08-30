package com.greenstory.foreststory.viewmodel.contents.setting

import androidx.lifecycle.ViewModel
import com.greenstory.foreststory.repository.contents.setting.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(val settingRepo : SettingRepository) : ViewModel()  {

    fun logOut(){
        settingRepo.logOut()
    }

}