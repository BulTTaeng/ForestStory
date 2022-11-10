package com.greenstory.foreststory.viewmodel.contents.setting.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.repository.contents.MountainRepository
import com.greenstory.foreststory.repository.contents.setting.add.AddAudioRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAudioViewModel @Inject constructor(val addAudioRepo: AddAudioRepository) : ViewModel() {

    private val _addAudioEvent = MutableEventFlow<AddAudioViewModel.Event>()
    val addAudioEvent = _addAudioEvent.asEventFlow()

    var audioString =""
    var mountainName = ""
    var detailInfo = DetailLocationInfo()

    fun upLoadAudio(audioEntity: AudioEntity){
        viewModelScope.launch {
            addAudioRepo.upLoadAudio(mountainName , detailInfo.name , audioString , audioEntity).collectLatest {
                _addAudioEvent.emit(Event.Success(it))
            }
        }
    }


    sealed class Event {
        data class Success(val success : Boolean) : Event()
    }
}