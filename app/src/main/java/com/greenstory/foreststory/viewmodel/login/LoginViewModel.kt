package com.greenstory.foreststory.viewmodel.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.repository.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginRepo : LoginRepository): ViewModel() {

    val userInfo = UserInfoEntity()
    var password : String = ""

    suspend fun emailSignUp(userInfo: UserInfoEntity , password: String) : Boolean{
        return loginRepo.emailSignUp(userInfo , password)
    }

    suspend fun emailLogIn(id : String , password : String) : Deferred<Boolean> =
        viewModelScope.async {
            val result  =  loginRepo.emailLogIn(id, password)
            result
        }

    fun getGoogleSignInClient(activity: Activity) : GoogleSignInClient {
        return loginRepo.getGoogleSignInClient(activity)
    }

    suspend fun firebaseAuthWithGoogle(googleSignInAccount: GoogleSignInAccount, ) : Int {
        return loginRepo.firebaseAuthWithGoogle(googleSignInAccount)
    }

    suspend fun writerUserToFireStore(userInfo: UserInfoEntity) : Boolean{
        return loginRepo.writerUserToFireStore(userInfo)
    }


}