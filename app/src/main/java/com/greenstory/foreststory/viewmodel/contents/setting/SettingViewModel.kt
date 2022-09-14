package com.greenstory.foreststory.viewmodel.contents.setting

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.greenstory.foreststory.repository.contents.setting.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(val settingRepo : SettingRepository) : ViewModel()  {

    var myInfo: LiveData<ArrayList<String>>? = null

    private val myReceiver = settingRepo.myReceiver

    suspend fun logOut(googleSignInClient: GoogleSignInClient) : Boolean{
        return settingRepo.logOut(googleSignInClient)
    }

    fun fetchMyReceiver() : LiveData<String>{
        return myReceiver
    }

    suspend fun getUserNameAndEmailProfileImage(){
        myInfo = settingRepo.getUserNameAndEmailProfileImage().asLiveData(viewModelScope.coroutineContext)
    }

    fun getGoogleSignInClient(activity: Activity) : GoogleSignInClient {
        return settingRepo.getGoogleSignInClient(activity)
    }

    suspend fun reCheckUser(email: String , password:String ,  acct: GoogleSignInAccount?) : Boolean{
        return settingRepo.reCheckUser(email , password , acct)
    }

    fun registerReceiver(){
        settingRepo.registerReceiver()
    }

    fun unRegister(){
        settingRepo.unRegister()
    }

}