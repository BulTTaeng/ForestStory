package com.greenstory.foreststory.viewmodel.audio

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.repository.audio.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(val audioRepo : AudioRepository) : ViewModel() {
    private val audioData = audioRepo.audioData
    private val audioLinkData = audioRepo.audioLinkData

    fun fetchAudioData(): LiveData<MutableList<AudioEntity>> {
        return audioData
    }

    fun fetchAudioLinkData(): LiveData<MutableList<MediaItem>> {
        return audioLinkData
    }

    suspend fun getAudioData(mountainName : String) : Boolean{
        return audioRepo.getAudioData(mountainName)
    }

}