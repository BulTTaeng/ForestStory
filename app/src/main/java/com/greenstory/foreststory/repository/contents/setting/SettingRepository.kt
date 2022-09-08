package com.greenstory.foreststory.repository.contents.setting

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.R
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.tasks.await

class SettingRepository {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val _mutableMyInfo = MutableLiveData<MutableList<String>>()

    val myInfo: LiveData<MutableList<String>>
        get() = _mutableMyInfo

    fun getGoogleSignInClient(activity : Activity) : GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    suspend fun logOut(googleSignInClient: GoogleSignInClient) : Boolean{
        var check : Boolean = false

        if(firebaseAuth.currentUser?.uid == null){
            return true
        }

        val type = _mutableMyInfo.value?.get(3)

        Log.d("11111" , Thread.currentThread().name)

        return try{
            if(type == "email"){
                firebaseAuth.signOut()
                check = true
                return true
            }
            else if (type == "google"){
                // Firebase sign out
                googleSignInClient.signOut().addOnCompleteListener{
                    check = it.isSuccessful
                    if(check){
                        firebaseAuth.signOut()
                    }
                    Log.d("wwwwwwwwwwwwwwww" , "Wwwwwwwwwwwwwwww")
                    Log.d("22222222222" , Thread.currentThread().name)
                }.await()
                Log.d("3333" , Thread.currentThread().name)
                return true
            }
            else if(type == "kakao"){
                firebaseAuth.signOut()

                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.d("카카오","카카오 로그아웃 실패")
                        check = false
                    }else {
                        check =true
                    }
                }
                return check
            }
            else{
                return false
            }
        }catch (e : Exception){
            false
        }
    }

    suspend fun getUserNameAndEmailProfileImage(userId: String): ArrayList<String> {
        val docRef = db.collection("user").document(userId)
        var resultString = ArrayList<String>()
        docRef.get().addOnSuccessListener {
            resultString.add(it["name"].toString())
            resultString.add(it["email"].toString())
            resultString.add(it["profile"].toString())
            resultString.add(it["type"].toString())
            _mutableMyInfo.value = resultString
        }.await()
        return resultString
    }
}