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
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(val audioRepo : AudioRepository) : ViewModel() {

    private var audioLinkData = audioRepo.audioLinkData

    var audioData: LiveData<MutableList<AudioEntity>>? =null

    fun fetchAudioLinkData(): LiveData<MutableList<MediaItem>> {
        return audioLinkData
    }

    suspend fun getAudioData(mountainName : String){
        audioData = audioRepo.getAudioData(mountainName).asLiveData(viewModelScope.coroutineContext)
    }

}