package com.greenstory.foreststory.repository.contents.setting

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.greenstory.foreststory.R
import com.greenstory.foreststory.utility.GlobalApplication
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SettingRepository {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var firabaseStorage = FirebaseStorage.getInstance()

    var resultString = ArrayList<String>()

    val _mutableReceiver = MutableLiveData<String>()

    val myReceiver: LiveData<String>
        get() = _mutableReceiver

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val msg = intent.getStringExtra("KEYVALUE")
            Log.d("wwwwww", msg.toString())
            _mutableReceiver.value = msg?.toString()
        }
    }

    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    suspend fun logOut(googleSignInClient: GoogleSignInClient): Boolean {
        var check: Boolean = false

        if (firebaseAuth.currentUser?.uid == null) {
            return true
        }

        val type = resultString[3]


        return try {
            if (type == "email") {
                firebaseAuth.signOut()
                check = true
                return true
            } else if (type == "google") {
                // Firebase sign out
                googleSignInClient.signOut().addOnCompleteListener {
                    check = it.isSuccessful
                    if (check) {
                        firebaseAuth.signOut()
                    }
                }.await()
                return true
            } else if (type == "kakao") {
                firebaseAuth.signOut()

                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.d("카카오", "카카오 로그아웃 실패")
                        check = false
                    } else {
                        check = true
                    }
                }
                return check
            } else {
                return false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserNameAndEmailProfileImage() = flow {

        try {
            val docRef = db.collection("user").document(firebaseAuth.currentUser!!.uid)
            resultString.clear()
            val it = docRef.get().await()

            resultString.add(it["name"].toString())
            resultString.add(it["email"].toString())
            resultString.add(it["profile"].toString())
            resultString.add(it["type"].toString())

            emit(resultString)
        } catch (e: Exception) {
            Log.d("getUserInfo", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun reCheckUser(email: String, password: String, acct: GoogleSignInAccount?): Boolean {
        return try {
            val type = resultString[3]

            if (type == "google") {
                val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
                firebaseAuth.currentUser!!
                    .reauthenticate(credential).await()
            } else {
                val credential = EmailAuthProvider.getCredential(email, password)
                firebaseAuth.currentUser!!
                    .reauthenticate(credential).await()
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    fun registerReceiver() {
        LocalBroadcastManager.getInstance(GlobalApplication.ApplicationContext()).registerReceiver(
            receiver, IntentFilter("DONE")
        )
    }

    fun unRegister() {
        LocalBroadcastManager.getInstance(GlobalApplication.ApplicationContext())
            .unregisterReceiver(
                receiver
            )
    }

    suspend fun isAdmin(): Boolean {
        return try {
            val info = db.collection("user").document(firebaseAuth.currentUser!!.uid).get().await()
            info["admin"] as Boolean
        } catch (e: Exception) {
            false
        }

    }

    suspend fun updateProfile(uri: Uri?, nickName: String) = flow {

        try {

            val admin = isAdmin()

            if(uri != null) {
                val downloadUri = addToStorage(uri)

                if(admin){
                    db.collection("commentator").document(firebaseAuth.currentUser!!.uid).update("profile" , downloadUri, "name" , nickName)
                }

                db.collection("user").document(firebaseAuth.currentUser!!.uid).update("profile" , downloadUri, "name" , nickName).await()
            }
            else{
                if(admin){
                    db.collection("commentator").document(firebaseAuth.currentUser!!.uid).update("name" , nickName)
                }
                db.collection("user").document(firebaseAuth.currentUser!!.uid).update( "name" , nickName).await()
            }

            emit(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseException -> {
                    Log.d("FirebaseException", e.toString())
                }
                else -> {
                    Log.d("Exception", e.toString())
                }
            }
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addToStorage(uri: Uri): String {


        var fileName =
            "${firebaseAuth.currentUser!!.uid}_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}" + "_.png"
        var imagesRef = firabaseStorage!!.reference.child("profiles/")
            .child(fileName)    //기본 참조 위치/images/${fileName}
        //이미지 파일 업로드
        var uploadTask = imagesRef.putFile(uri)
        //uploadTaskList.add(uploadTask)

        val downloadUri = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imagesRef.downloadUrl
        }.await()



        return downloadUri.toString()
    }
}