package com.greenstory.foreststory.repository.contents

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.model.contents.CommentatorEntity
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.model.contents.MountainEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CommentatorRepository {

    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    val commentators = ArrayList<CommentatorDto>()
    val foundCommentators = ArrayList<CommentatorDto>()
    var temp = CommentatorEntity()

    suspend fun getCommentators() = flow{
            commentators.clear()
            try {

                db.collection("commentator").orderBy("likedNum" , Query.Direction.DESCENDING).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (it in task.result) {
                                temp = it.toObject(CommentatorEntity::class.java)
                                commentators.add(temp.mapper())
                            }
                        }
                    }.await()
                emit(commentators)
            } catch (e: Exception) {
                Log.d("getMountain Exception", e.toString())
            }

    }.flowOn(Dispatchers.IO)

    fun CommentatorEntity.mapper( ): CommentatorDto =
        CommentatorDto(audio , likedNum, mountain, id, name, profile, explain)

    suspend fun searchCommentators(str : String) = flow{
        foundCommentators.clear()
        try {
            //여기서도 인기순으로 나와야 하나?
            db.collection("commentator").orderBy("likedNum" , Query.Direction.DESCENDING).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (it in task.result) {
                            temp = it.toObject(CommentatorEntity::class.java)
                            if(temp.name.contains(str)) {
                                foundCommentators.add(temp.mapper())
                            }
                        }
                    }
                }.await()

            emit(foundCommentators)
        }catch (e: Exception) {
            Log.d("getMountain Exception", e.toString())
        }
    }.flowOn(Dispatchers.IO)


}