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
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.greenstory.foreststory.R
import com.greenstory.foreststory.model.contents.CommentatorEntity
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddProgramRepository {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var firabaseStorage = FirebaseStorage.getInstance()

    fun upLoadProgram(detailLocationInfo: DetailLocationInfo , mountainName : String , isImageEdited : Boolean) = flow{

        try {
            //Upload to firestore story
            db.collection("story").document(mountainName).update("detailLocation" , FieldValue.arrayUnion(detailLocationInfo.name)).await()
            //Upload to Storage
            if(isImageEdited) {
                val image = addToStorage(Uri.parse(detailLocationInfo.image))
                detailLocationInfo.image = image
            }
            db.collection("story").document(mountainName).collection(detailLocationInfo.name).document(detailLocationInfo.name).set(detailLocationInfo).await()

            // get commentator data
            val commentatorSnapshot = db.collection("commentator").document(firebaseAuth.currentUser!!.uid).get().await()
            val commentatorInfo = commentatorSnapshot.toObject(CommentatorEntity::class.java)

            //manipulates & update commentator data
            db.collection("commentator").document(firebaseAuth.currentUser!!.uid).update("mountains" , FieldValue.arrayUnion(mountainName)).await()
            val programs = commentatorInfo!!.mountain[mountainName]
            if(programs!=null && !programs.contains(detailLocationInfo.name)) {
                programs.add(detailLocationInfo.name)
                commentatorInfo.mountain[mountainName] = programs
            }

            //upload commentator data
            db.collection("commentator").document(firebaseAuth.currentUser!!.uid).set(commentatorInfo).await()
            emit(true)
        }catch (e : Exception){
            Log.w("UploadProgram" , e.toString() )
            emit(false)
        }

    }.flowOn(Dispatchers.IO)

    suspend fun addToStorage(uri: Uri): String {


        var fileName =
            "${firebaseAuth.currentUser!!.uid}_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}" + "_.png"
        var imagesRef = firabaseStorage.reference.child(firebaseAuth.currentUser?.uid.toString() + "/")
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