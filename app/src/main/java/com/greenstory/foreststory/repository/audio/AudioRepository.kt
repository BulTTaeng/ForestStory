package com.greenstory.foreststory.repository.audio

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.model.audio.Audios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AudioRepository {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val audioList = ArrayList<AudioEntity>()
    val audioLink = ArrayList<MediaItem>()
    var audios = Audios()

    val _mutableSingleLink = MutableLiveData<MediaItem>()

    val singleAudioLink : LiveData<MediaItem>
        get() = _mutableSingleLink


    suspend fun getAudioData(mountainName : String , detailName : String ) = flow{

        audioList.clear()
        audioLink.clear()

        var temp = AudioEntity()
        var mediaItem : MediaItem

        try {
            var stories = db.collection("story").document(mountainName).collection(detailName)
                .orderBy("likeNum").get().await()

            for (it in stories) {
                temp = it.toObject(AudioEntity::class.java)
                audioList.add(temp)
                mediaItem =
                    MediaItem.fromUri(Uri.parse(temp.link))
                audioLink.add(mediaItem)
            }

            audios.audioLink = audioLink
            audios.audioList = audioList
            emit(audios)
        }catch (e : Exception){
            Log.d("getAudio" , e.toString())
        }

    }.flowOn(Dispatchers.IO)

    suspend fun getAudioDataWithInfo(mountainName : String , detailName : String , audioIdList : ArrayList<String>) = flow{

        var temp = AudioEntity()
        var mediaItem : MediaItem

        try {
            val stories = db.collection("story").document(mountainName).collection(detailName).get().await()

            for(it in stories){
                if(audioIdList.contains(it.id)){
                    temp = it.toObject(AudioEntity::class.java)
                    audioList.add(temp)
                    mediaItem =
                        MediaItem.fromUri(Uri.parse(temp.link))
                    audioLink.add(mediaItem)
                }
            }

            audios.audioLink = audioLink
            audios.audioList = audioList
            emit(audios)
        }catch (e : Exception){
            Log.d("getAudio" , e.toString())
        }

    }.flowOn(Dispatchers.IO)

}