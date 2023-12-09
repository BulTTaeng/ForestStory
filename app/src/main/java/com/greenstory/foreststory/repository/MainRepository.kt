package com.greenstory.foreststory.repository

//import com.kakao.sdk.user.UserApiClient
//import kotlinx.coroutines.CoroutineScope
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainRepository {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    //0-> 로그인 , 1-> 마이 페이지지
   fun isLogin() : Int{
        val user = firebaseAuth.currentUser

        if(user == null){
            // 카카오 로그인 정보 확인
            return 0
        }
        else{
            Log.d("정보있음", "${user.displayName},${user.uid},${user.email}")
            return 1
        }
    }
}