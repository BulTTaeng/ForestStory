package com.greenstory.foreststory.viewmodel.contents.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenstory.foreststory.model.setting.CommentatorVerify
import com.greenstory.foreststory.repository.contents.setting.SettingRepository
import com.greenstory.foreststory.repository.contents.setting.VerifyRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(val verifyRepo : VerifyRepository) : ViewModel(){

    private val _verifyEvent = MutableEventFlow<VerifyViewModel.Event>()
    val verifyEvent = _verifyEvent.asEventFlow()

    fun verifyForestCommentator(commentatorVerify : CommentatorVerify){
        viewModelScope.launch {
            verifyRepo.verifyForestCommentator(commentatorVerify).collectLatest {
                _verifyEvent.emit(VerifyViewModel.Event.Success(it))
            }

        }
    }

    sealed class Event {
        data class Success(val success : Boolean) : Event()
    }

}