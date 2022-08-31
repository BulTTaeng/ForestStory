package com.greenstory.foreststory.viewmodel.login

import androidx.lifecycle.ViewModel
import com.greenstory.foreststory.model.UserInfoEntity
import com.greenstory.foreststory.repository.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginRepo : LoginRepository): ViewModel() {

    suspend fun emailSignUp(userInfo: UserInfoEntity) : Boolean{
        return loginRepo.emailSignUp(userInfo)
    }

    suspend fun emailLogIn(id : String , password : String) : Boolean{
        return loginRepo.emailLogIn(id, password)
    }
}