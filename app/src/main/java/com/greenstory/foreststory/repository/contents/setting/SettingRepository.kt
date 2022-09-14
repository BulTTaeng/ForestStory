package com.greenstory.foreststory.repository.contents.setting

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.R
import com.greenstory.foreststory.utility.GlobalApplication
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.tasks.await

class SettingRepository {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val _mutableMyInfo = MutableLiveData<MutableList<String>>()
    val _mutableReceiver = MutableLiveData<String>()

    val myInfo: LiveData<MutableList<String>>
        get() = _mutableMyInfo

    val myReceiver: LiveData<String>
        get() = _mutableReceiver

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val msg = intent.getStringExtra("KEYVALUE")
            Log.d("wwwwww" , msg.toString())
            _mutableReceiver.value = msg?.toString()
        }
    }

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
                }.await()
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

    suspend fun reCheckUser(email: String , password:String ,  acct: GoogleSignInAccount?) : Boolean{
        return try {
            val type = _mutableMyInfo.value?.get(3)

            if(type == "google"){
                val credential = GoogleAuthProvider.getCredential(acct!!.idToken , null)
                firebaseAuth.currentUser!!
                    .reauthenticate(credential).await()
                Log.d("heree" , "hhhhhhh")
            }

            else{
                val credential = EmailAuthProvider.getCredential(email, password)
                firebaseAuth.currentUser!!
                    .reauthenticate(credential).await()
            }

            true
        }catch (e : Exception){
            false
        }
    }

    fun registerReceiver(){
        LocalBroadcastManager.getInstance(GlobalApplication.ApplicationContext()).registerReceiver(
            receiver, IntentFilter("DONE")
        )
    }

    fun unRegister(){
        LocalBroadcastManager.getInstance(GlobalApplication.ApplicationContext()).unregisterReceiver(
            receiver
        )
    }
}