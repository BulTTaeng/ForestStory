package com.greenstory.foreststory.repository.login

import android.app.Activity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.R
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import kotlinx.coroutines.tasks.await

class LoginRepository {

    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    suspend fun emailSignUp(userInfo : UserInfoEntity , password: String) : Boolean {
        try {
            var check = false
            firebaseAuth!!.createUserWithEmailAndPassword(userInfo.email, password).await()
            userInfo.userId = firebaseAuth.currentUser!!.uid
            check = writerUserToFireStore(userInfo)
            return check
        } catch (exception : Exception) {
            Log.w("[EmailSignUp]", "Email Sign Up Exception!")
            return false
        }
    }

    suspend fun writerUserToFireStore(userInfo: UserInfoEntity) : Boolean{
        try {
            db.collection("user").document(userInfo.userId).set(userInfo).await()
            return true
        }catch (e: Exception){
            Log.w("[write FireStore]", "FireStore write Exception!")
            return false
        }
    }

    suspend fun emailLogIn(id : String , password : String) : Boolean{
        return try {
            firebaseAuth!!.signInWithEmailAndPassword(id , password).await()
            return true
        } catch (exception : Exception) {
            Log.w("[EmailSignIn]", "Email Sign In Exception!")
            return false
        }
    }

    fun getGoogleSignInClient(activity : Activity) : GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    // firebaseAuthWithGoogle
    // 0 : 원래 로그인 된 계정, 1 : 처음 구글 로그인 시도, 2 : 구글 로그인 실패
    suspend fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) : Int {
        val username: String = acct.displayName.toString()
        val email: String = acct.email.toString()
        var isSuccess : Boolean = false
        var result : Int = -1

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.w("Google Login", "firebaseAuthWithGoogle 성공", task.exception)
                    isSuccess = true
                } else {
                    Log.w("Google Login", "firebaseAuthWithGoogle 실패", task.exception)
                    result=2
                }
            }.await()
        when (isSuccess) {
            true -> {
                db.collection("user").document(firebaseAuth.currentUser!!.uid).get().addOnCompleteListener { task->
                    if (task.result["name"] as String? == null) {
                        result = 1
                    } else {
                        result = 0
                    }
                }.await()
            }
            false ->{
                4
            }
        }
        return result
    }


}