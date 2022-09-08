package com.greenstory.foreststory.viewmodel.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.repository.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginRepo : LoginRepository): ViewModel() {

    suspend fun emailSignUp(userInfo: UserInfoEntity , password: String) : Boolean{
        return loginRepo.emailSignUp(userInfo , password)
    }

    suspend fun emailLogIn(id : String , password : String) : Boolean{
        return loginRepo.emailLogIn(id, password)
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