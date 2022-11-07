package com.greenstory.foreststory.utility.interfaces.login

import android.content.Intent
import com.greenstory.foreststory.model.userinfo.UserInfoLogin

//Email 로그인을 제외한 나머지 로그인을 위한 인터페이스
interface OtherLoginInterface {
    suspend fun signUpOrLogin(data : Intent?) : UserInfoLogin
}