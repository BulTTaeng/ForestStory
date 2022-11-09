package com.greenstory.foreststory.viewmodel.contents

import android.util.Log
import androidx.lifecycle.*
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.repository.contents.MountainRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(val mountainRepo: MountainRepository) : ViewModel() {

    private val _mountainData = MutableEventFlow<Event>()
    val mountainData = _mountainData.asEventFlow()

    fun getDetailLoc(mountainName: String){
        viewModelScope.launch {
            mountainRepo.getDetailLoc(mountainName).collectLatest {
                _mountainData.emit(Event.DetailLocations(it))
            }
        }
    }

    fun getDetailLocContains(mountainName: String , containsList : ArrayList<String>){
        viewModelScope.launch {
            mountainRepo.getDetailLocContains(mountainName , containsList).collectLatest {
                _mountainData.emit(Event.DetailLocations(it))
            }
        }
    }

    fun getMountainData() {
        viewModelScope.launch {
            mountainRepo.getMountainData().collectLatest {
                _mountainData.emit(Event.Mountains(it))
            }
        }
    }

    fun getMountainWithDistance(lati: Double, longi: Double) {
        viewModelScope.launch {
            mountainRepo.getMountainWithDistance(lati, longi).collectLatest() {
                _mountainData.emit(Event.Mountains(it))
            }
        }

    }

    fun getMountainDataContain(list : ArrayList<String>){
        viewModelScope.launch {
            mountainRepo.getMountainDataContain(list).collectLatest() {
                _mountainData.emit(Event.Mountains(it))
            }
        }
    }

    fun getMountainDataContain(str : String){
        viewModelScope.launch {
            mountainRepo.getMountainDataContain(str).collectLatest() {
                _mountainData.emit(Event.Mountains(it))
            }
        }
    }

    sealed class Event {
        data class Mountains(val mountains : ArrayList<MountainDto>) : Event()
        data class DetailLocations(val locs : ArrayList<DetailLocationInfo>) : Event()
    }

}