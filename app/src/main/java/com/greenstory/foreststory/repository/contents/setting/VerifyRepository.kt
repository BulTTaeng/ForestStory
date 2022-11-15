package com.greenstory.foreststory.repository.contents.setting

import android.os.Parcelable
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.model.contents.CommentatorEntity
import com.greenstory.foreststory.model.setting.CommentatorVerify
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class VerifyRepository {

    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    fun verifyForestCommentator(commentatorVerify : CommentatorVerify) =flow{
        try {
            //TODO :: api 호출로 인증 확인

            val userDocument = db.collection("user").document(firebaseAuth.currentUser!!.uid).get().await()
            val userInfo = userDocument.toObject(UserInfoEntity::class.java)
            val commentatorInfo = userInfo?.mapper()
            commentatorInfo?.let {
                db.collection("commentator").document(firebaseAuth.currentUser!!.uid).set(commentatorInfo).await()
                db.collection("user").document(firebaseAuth.currentUser!!.uid).update("admin" , true)
            }
            emit(true)
        }catch (e : Exception){
            Log.d("Verify" , e.toString())
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    fun UserInfoEntity.mapper( ): CommentatorEntity =
        CommentatorEntity(0L, mutableMapOf<String, ArrayList<String>>(), userId, name, profile, explain, "" , ArrayList<String>() , nickName)
}