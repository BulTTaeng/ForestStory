package com.greenstory.foreststory.viewmodel.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.model.contents.MountainEntity
import com.greenstory.foreststory.repository.contents.MountainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(val mountainRepo : MountainRepository) : ViewModel() {

    private val mountainData = mountainRepo.mountainData

    fun fetchMountainData(): LiveData<MutableList<MountainDto>> {
        return mountainData
    }

    fun setMountainData(data : ArrayList<MountainDto>){
        mountainRepo.setMountainData(data)
    }

    suspend fun getMountainData() : Boolean{
        return mountainRepo.getMountainData()
    }

}