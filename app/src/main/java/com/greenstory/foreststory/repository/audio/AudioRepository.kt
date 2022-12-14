package com.greenstory.foreststory.repository.audio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
                .orderBy("sequence").get().await()

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

    suspend fun deleteAudio(mountainName : String , detailName : String , did : String) = flow{
        try {
            db.collection("story").document(mountainName).collection(detailName).document(did).delete().await()
            emit(true)
        }catch (e : Exception){
            Log.d("editAudioName" , e.toString())
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun editAudioName(pos : Int, mountainName : String , detailName : String , did : String , str : String) = flow{
        try {
            db.collection("story").document(mountainName).collection(detailName).document(did).update("audioName" , str).await()
            emit(Pair<Int , String>( pos, str))
        }catch (e : Exception){
            Log.d("editAudioName" , e.toString())
            emit(Pair<Int , String>( -1, ""))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getBitmapFromURL(src: String?) =flow{
        try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.getInputStream()
            emit(BitmapFactory.decodeStream(input))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

}