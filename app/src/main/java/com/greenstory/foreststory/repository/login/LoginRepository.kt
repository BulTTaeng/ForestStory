package com.greenstory.foreststory.repository.login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.model.UserInfoEntity
import kotlinx.coroutines.tasks.await

class LoginRepository {

    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    suspend fun emailSignUp(userInfo : UserInfoEntity) : Boolean {
        try {
            var check = false
            firebaseAuth!!.createUserWithEmailAndPassword(userInfo.email, userInfo.password).await()
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


}