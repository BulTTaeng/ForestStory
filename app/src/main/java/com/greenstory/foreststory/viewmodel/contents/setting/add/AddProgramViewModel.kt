package com.greenstory.foreststory.viewmodel.contents.setting.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.repository.contents.setting.add.AddProgramRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProgramViewModel @Inject constructor(val addProgramRepo: AddProgramRepository) : ViewModel() {

    private val _programEvent = MutableEventFlow<AddProgramViewModel.Event>()
    val programEvent = _programEvent.asEventFlow()

    var detailLocationInfo = DetailLocationInfo()
    var mountainName : String = ""
    var isImageEdited = false

    fun upLoadProgram(){
        viewModelScope.launch {
            addProgramRepo.upLoadProgram(detailLocationInfo , mountainName , isImageEdited).collectLatest {
                _programEvent.emit(Event.Success(it))
            }
        }
    }

    sealed class Event {
        data class Success(val success : Boolean) : Event()
    }
}