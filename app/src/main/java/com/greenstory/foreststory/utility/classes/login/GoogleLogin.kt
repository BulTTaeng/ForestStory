package com.greenstory.foreststory.utility.classes.login

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.model.userinfo.UserInfoLogin
import com.greenstory.foreststory.utility.interfaces.login.OtherLoginInterface
import kotlinx.coroutines.tasks.await

class GoogleLogin : OtherLoginInterface {

    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override suspend fun signUpOrLogin(data : Intent?) : UserInfoLogin {
        var result = UserInfoLogin()

        return try {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            result.email = account.email.toString()
            result.name = account.displayName.toString()
            result.type = "google"

            firebaseAuth.signInWithCredential(credential).await()
            val userInfoDocument = db.collection("user").document(firebaseAuth.currentUser!!.uid).get().await()

            if(userInfoDocument["name"] as String? == null){
                result.exist = 1
            }else{
                result.exist = 0
            }

            result
        }catch (e:Exception){
            Log.d("googleLoginError" , e.toString())
            UserInfoLogin("" ,"" , 2,"")
        }
    }

}