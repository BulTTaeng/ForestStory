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

    val _mutableSingleLink = MutableLiveData<MediaItem>()

    val singleAudioLink : LiveData<MediaItem>
        get() = _mutableSingleLink

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

    suspend fun getAudioDataWithInfo(mountainName : String , audioId : String) = flow{

        var temp = AudioEntity()
        var mediaItem : MediaItem

        try {
            db.collection("story").document(mountainName).collection(mountainName).document(audioId)
                .get().addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        temp = it.result.toObject(AudioEntity::class.java)!!

                        mediaItem =
                            MediaItem.fromUri(Uri.parse(temp.link))
                        _mutableSingleLink.value = mediaItem
                    }
                }.await()
            emit(audioList)
        }catch (e : Exception){
            Log.d("getAudio" , e.toString())
        }

    }.flowOn(Dispatchers.IO)

}