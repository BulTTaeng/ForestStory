package com.greenstory.foreststory.repository.contents

import android.os.Parcelable
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.greenstory.foreststory.model.contents.*
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import kotlinx.android.parcel.Parcelize
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

                val commentatorDocuments = db.collection("commentator").orderBy("likedNum" , Query.Direction.DESCENDING).get().await()

                for (it in commentatorDocuments) {
                    temp = it.toObject(CommentatorEntity::class.java)
                    commentators.add(temp.mapper())
                }
                emit(commentators)
            } catch (e: Exception) {
                Log.d("getMountain Exception", e.toString())
            }

    }.flowOn(Dispatchers.IO)


    suspend fun searchCommentators(str : String) = flow{
        foundCommentators.clear()
        try {
            //여기서도 인기순으로 나와야 하나?
            val commentatorDocuments = db.collection("commentator").orderBy("likedNum" , Query.Direction.DESCENDING).get().await()

            for (it in commentatorDocuments) {
                temp = it.toObject(CommentatorEntity::class.java)
                if(temp.hashTag.contains(str)) {
                    foundCommentators.add(temp.mapper())
                }
            }

            emit(foundCommentators)
        }catch (e: Exception) {
            Log.d("Search Exception", e.toString())
        }
    }.flowOn(Dispatchers.IO)


    suspend fun getUserInfo() = flow {
        val userDoc = db.collection("user").document(firebaseAuth.currentUser!!.uid).get().await()
        val admin = userDoc.toObject(UserInfoEntity::class.java)
        emit(admin)
    }.flowOn(Dispatchers.IO)


    suspend fun getMyCommentator() =flow{
        val my = db.collection("commentator").document(firebaseAuth.currentUser!!.uid).get().await()
        val info = my.toObject(CommentatorEntity::class.java)
        val infoDto = info?.mapper()
        emit(infoDto)
    }.flowOn(Dispatchers.IO)

}