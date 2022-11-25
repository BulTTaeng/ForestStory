package com.greenstory.foreststory.viewmodel.contents.setting.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.repository.contents.setting.delete.DeleteProgramRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteProgramViewModel @Inject constructor(val deleteProgramRepo: DeleteProgramRepository) : ViewModel() {

    private val _programDeleteEvent = MutableEventFlow<DeleteProgramViewModel.Event>()
    val programDeleteEvent = _programDeleteEvent.asEventFlow()

    var isImageEdited = false

    //TODO:: 익셉션 처리!!!
    fun deleteProgram(detailLocationInfo : DetailLocationInfo , mountainName : String){
        viewModelScope.launch {
            deleteProgramRepo.deleteProgram(detailLocationInfo , mountainName).collectLatest {
                _programDeleteEvent.emit(Event.Success(it))
            }
        }
    }

    sealed class Event {
        data class Success(val success : Boolean) : Event()
    }
}