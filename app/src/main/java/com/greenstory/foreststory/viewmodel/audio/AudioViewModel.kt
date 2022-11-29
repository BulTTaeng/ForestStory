package com.greenstory.foreststory.viewmodel.audio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.model.audio.Audios
import com.greenstory.foreststory.repository.audio.AudioRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(val audioRepo: AudioRepository) : ViewModel() {

    private val _audioData = MutableEventFlow<AudioViewModel.Event>()
    val audioData = _audioData.asEventFlow()

    fun getAudioData(mountainName: String , detailName : String) {
        viewModelScope.launch {
            audioRepo.getAudioData(mountainName, detailName).collectLatest {
                _audioData.emit(Event.AudiosList(it))
            }
        }
    }

    fun getAudioDataWithInfo(mountainName: String, detailName : String , audioIdList: ArrayList<String>) {
        viewModelScope.launch {
            audioRepo.getAudioDataWithInfo(mountainName, detailName , audioIdList).collectLatest {
                _audioData.emit(Event.AudiosList(it))
            }
        }
    }

    fun editAudioName(pos : Int , mountainName: String, detailName : String , did : String , str : String){
        viewModelScope.launch {
            audioRepo.editAudioName(pos , mountainName, detailName , did , str).collectLatest {
                _audioData.emit(Event.EditedLoc(it))
            }
        }
    }

    fun deleteAudio (mountainName: String, detailName : String , did : String){
        viewModelScope.launch {
            audioRepo.deleteAudio(mountainName, detailName , did).collectLatest {
                _audioData.emit(Event.Success(it))
            }
        }
    }

    fun getBitmapFromURL(src: String?) {

        viewModelScope.launch {
            audioRepo.getBitmapFromURL(src).collectLatest {
                _audioData.emit(Event.BitmapImage(it))
            }
        }
    }

    sealed class Event {
        data class AudiosList(val audios: Audios) : Event()
        data class EditedLoc(val locString : Pair<Int , String>) : Event()
        data class Success(val success : Boolean) : Event()
        data class BitmapImage(val bitmap: Bitmap?) : Event()
    }

}