package com.greenstory.foreststory.viewmodel.audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.repository.audio.AudioRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(val audioRepo: AudioRepository) : ViewModel() {

    private var audioLinkData = audioRepo.audioLinkData

    private val _audioData = MutableEventFlow<AudioViewModel.Event>()
    val audioData = _audioData.asEventFlow()

    fun fetchAudioLinkData(): LiveData<MutableList<MediaItem>> {
        return audioLinkData
    }

    fun getAudioData(mountainName: String) {
        viewModelScope.launch {
            audioRepo.getAudioData(mountainName).collectLatest {
                _audioData.emit(Event.Audios(it))
            }
        }
    }

    fun getAudioDataWithInfo(mountainName: String, audioIdList: ArrayList<String>) {
        viewModelScope.launch {
            audioRepo.getAudioDataWithInfo(mountainName, audioIdList).collectLatest {
                _audioData.emit(Event.Audios(it))
            }
        }
    }

    sealed class Event {
        data class Audios(val audios: ArrayList<AudioEntity>) : Event()
    }

}