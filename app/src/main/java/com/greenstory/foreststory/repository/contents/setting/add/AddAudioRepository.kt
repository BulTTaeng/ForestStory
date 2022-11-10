package com.greenstory.foreststory.repository.contents.setting.add

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
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.utility.GlobalApplication
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddAudioRepository {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance()

    suspend fun upLoadAudio(mountainName : String , programName : String , audioString : String , audioEntity : AudioEntity) = flow {
        try {
            audioEntity.link = addToStorage(Uri.parse(audioString))
            db.collection("story").document(mountainName).collection(programName).add(audioEntity).await()
            emit(true)
        }catch (e : Exception){
            Log.w("upLoadAudio" , e.toString())
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addToStorage(uri: Uri): String {

        //TODO:: 오디오 파일 타입 받아야됨
        val fileName =
            "${firebaseAuth.currentUser!!.uid}_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}" + "_.mp3"
        val audioRef = firebaseStorage.reference.child(firebaseAuth.currentUser?.uid.toString() + "/")
            .child(fileName)

        //파일 업로드
        val uploadTask = audioRef.putFile(uri)
        //uploadTaskList.add(uploadTask)

        val downloadUri = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            audioRef.downloadUrl
        }.await()



        return downloadUri.toString()
    }
}