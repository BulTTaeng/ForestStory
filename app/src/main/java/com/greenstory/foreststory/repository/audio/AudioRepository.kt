package com.greenstory.foreststory.repository.audio

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.model.audio.AudioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AudioRepository {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val audioList = ArrayList<AudioEntity>()
    val audioLink = ArrayList<MediaItem>()

    val _mutableAudioLink = MutableLiveData<MutableList<MediaItem>>()

    val audioLinkData: LiveData<MutableList<MediaItem>>
        get() = _mutableAudioLink

    suspend fun getAudioData(mountainName : String) = flow{

        audioList.clear()
        audioLink.clear()

        var temp = AudioEntity()
        var mediaItem : MediaItem

        try {
            db.collection("story").document(mountainName).collection(mountainName)
                .orderBy("likeNum").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (it in task.result) {
                        temp = it.toObject(AudioEntity::class.java)
                        audioList.add(temp)
                        mediaItem =
                            MediaItem.fromUri(Uri.parse(temp.link))
                        audioLink.add(mediaItem)
                    }
                    _mutableAudioLink.value = audioLink
                }
            }.await()
            emit(audioList)
        }catch (e : Exception){
            Log.d("getAudio" , e.toString())
        }

    }.flowOn(Dispatchers.IO)

}