package com.greenstory.foreststory.viewmodel.contents.setting

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.repository.contents.setting.SettingRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(val settingRepo : SettingRepository) : ViewModel()  {

    private val myReceiver = settingRepo.myReceiver

    private val _myInfo = MutableEventFlow<Event>()
    val myInfo = _myInfo.asEventFlow()

    suspend fun logOut(googleSignInClient: GoogleSignInClient) : Boolean{
        return settingRepo.logOut(googleSignInClient)
    }

    fun fetchMyReceiver() : LiveData<String>{
        return myReceiver
    }

    fun getUserNameAndEmailProfileImage(){
        viewModelScope.launch {
            settingRepo.getUserNameAndEmailProfileImage().collectLatest {
                _myInfo.emit(Event.Info(it))
            }

        }
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

    sealed class Event {
        data class Info(val info : ArrayList<String>) : Event()
    }

}