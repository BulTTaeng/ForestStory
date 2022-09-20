package com.greenstory.foreststory.viewmodel.audio

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.repository.audio.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(val audioRepo : AudioRepository) : ViewModel() {

    private var audioLinkData = audioRepo.audioLinkData

    var audioData: LiveData<MutableList<AudioEntity>>? =null

    fun fetchAudioLinkData(): LiveData<MutableList<MediaItem>> {
        return audioLinkData
    }

    fun getAudioData(mountainName : String){
        viewModelScope.launch {
            audioData =
                audioRepo.getAudioData(mountainName).asLiveData(viewModelScope.coroutineContext)
        }
    }

    fun getAudioDataWithInfo(mountainName : String , audioIdList : ArrayList<String>){
        viewModelScope.launch {
            audioData =
                audioRepo.getAudioDataWithInfo(mountainName , audioIdList).asLiveData(viewModelScope.coroutineContext)
        }
    }

}