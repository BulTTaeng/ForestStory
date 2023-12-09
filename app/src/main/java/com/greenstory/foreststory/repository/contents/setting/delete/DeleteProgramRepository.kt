package com.greenstory.foreststory.repository.contents.setting.delete

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.greenstory.foreststory.model.contents.CommentatorEntity
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.Executor

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