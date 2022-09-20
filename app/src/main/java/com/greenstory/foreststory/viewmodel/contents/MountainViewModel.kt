package com.greenstory.foreststory.viewmodel.contents

import android.util.Log
import androidx.lifecycle.*
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.model.contents.MountainEntity
import com.greenstory.foreststory.repository.contents.MountainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class MountainViewModel @Inject constructor(val mountainRepo: MountainRepository) : ViewModel() {

    val _mutableMountainData = MutableLiveData<MutableList<MountainDto>>()
    val mountainData: LiveData<MutableList<MountainDto>>
        get() = _mutableMountainData


    fun getMountainData() {
        viewModelScope.launch {
            mountainRepo.getMountainData().collectLatest {
                _mutableMountainData.value = it
            }
        }
    }

    fun getMountainWithDistance(lati: Double, longi: Double) {
        viewModelScope.launch {
            mountainRepo.getMountainWithDistance(lati, longi).collectLatest() {
                _mutableMountainData.value = it
            }
        }

    }

    fun getMountainDataContain(list : ArrayList<String>){
        viewModelScope.launch {
            mountainRepo.getMountainDataContain(list).collectLatest() {
                _mutableMountainData.value = it
            }
        }
    }

}