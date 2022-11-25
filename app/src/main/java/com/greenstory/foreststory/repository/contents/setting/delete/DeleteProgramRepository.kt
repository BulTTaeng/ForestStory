package com.greenstory.foreststory.repository.contents.setting.delete

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
import androidx.annotation.WorkerThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.*
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
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.collections.ArrayList

class DeleteProgramRepository {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var firabaseStorage = FirebaseStorage.getInstance()

    fun deleteProgram(detailLocationInfo: DetailLocationInfo , mountainName : String) = flow{

        try {
            val collectionRef = db.collection("story").document(mountainName).collection(detailLocationInfo.name).get().await()

            for(doc in collectionRef){
                doc.reference.delete()
            }

            val docRef = db.collection("story").document(mountainName).update("detailLocation" , FieldValue.arrayRemove(detailLocationInfo.name)).await()
            deleteFromUserInfo(detailLocationInfo , mountainName)
            emit(true)
        }catch (e : Exception){
            Log.w("deleteProgram" , e.toString() )
            emit(false)
        }

    }.flowOn(Dispatchers.IO)

    @Throws(Exception::class)
    suspend fun deleteFromUserInfo(detailLocationInfo: DetailLocationInfo , mountainName : String){
        val commentatorDoc = db.collection("commentator").document(firebaseAuth.currentUser?.uid.toString()).get().await()
        val commentatorInfo = commentatorDoc.toObject(CommentatorEntity::class.java)

        val programs = commentatorInfo!!.mountain[mountainName]
        if(programs!=null) {
            programs.remove(detailLocationInfo.name)
            commentatorInfo.mountain[mountainName] = programs

            if(programs.isEmpty()){
                commentatorInfo.mountains.remove(mountainName)
            }
        }

        //upload commentator data
        db.collection("commentator").document(firebaseAuth.currentUser!!.uid).set(commentatorInfo).await()
    }

    private fun deleteCollection(collection: CollectionReference, executor: Executor) {
        Tasks.call(executor) {
            val batchSize = 10
            var query = collection.orderBy(FieldPath.documentId()).limit(batchSize.toLong())
            var deleted = deleteQueryBatch(query)

            while (deleted.size >= batchSize) {
                val last = deleted[deleted.size - 1]
                query = collection.orderBy(FieldPath.documentId()).startAfter(last.id).limit(batchSize.toLong())

                deleted = deleteQueryBatch(query)
            }

            null
        }
    }

    @WorkerThread
    @Throws(Exception::class)
    private fun deleteQueryBatch(query: Query): List<DocumentSnapshot> {
        val querySnapshot = Tasks.await(query.get())

        val batch = query.firestore.batch()
        for (snapshot in querySnapshot) {
            batch.delete(snapshot.reference)
        }
        Tasks.await(batch.commit())

        return querySnapshot.documents
    }

}