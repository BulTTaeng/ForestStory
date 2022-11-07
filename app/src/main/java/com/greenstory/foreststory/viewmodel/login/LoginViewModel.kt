package com.greenstory.foreststory.viewmodel.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.repository.login.LoginRepository
import com.greenstory.foreststory.utility.interfaces.login.OtherLoginInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginRepo : LoginRepository): ViewModel() {

    var userInfo = UserInfoEntity()
    var password : String = ""

    suspend fun emailSignUp() : Boolean{
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

    suspend fun firebaseAuthWithGoogle(googleSignInAccount: GoogleSignInAccount ) : Int {
        return loginRepo.firebaseAuthWithGoogle(googleSignInAccount)
    }

    suspend fun writerUserToFireStore() : Boolean{
        return try {
            loginRepo.writerUserToFireStore(userInfo)
            true
        }catch (e : Exception){
            false
        }
    }

    suspend fun firebaseAuthing(data : Intent?, otherLogins : OtherLoginInterface) : Int{
        userInfo = loginRepo.firebaseAuthing(data , otherLogins)

        return when (userInfo.explain) {
            "error" -> {
                2
            }
            "exist" -> {
                0
            }
            else -> {
                1
            }
        }
    }


}