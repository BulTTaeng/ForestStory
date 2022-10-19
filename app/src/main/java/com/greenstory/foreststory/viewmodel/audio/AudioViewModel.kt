package com.greenstory.foreststory.viewmodel.audio

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

    sealed class Event {
        data class AudiosList(val audios: Audios) : Event()
    }

}