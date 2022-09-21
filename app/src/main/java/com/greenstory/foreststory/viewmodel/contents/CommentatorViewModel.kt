package com.greenstory.foreststory.viewmodel.contents

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.repository.contents.CommentatorRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class CommentatorViewModel @Inject constructor(val commentatorRepo : CommentatorRepository) : ViewModel() {



    private val _commentatorData = MutableEventFlow<Event>()
    val commentatorData = _commentatorData.asEventFlow()

    private val _foundCommentatorData = MutableEventFlow<Event>()
    val foundCommentatorData = _foundCommentatorData.asEventFlow()

    fun getCommentators(){
        viewModelScope.launch {
            commentatorRepo.getCommentators().collectLatest() {
                _commentatorData.emit(Event.Commentators(it))
            }
        }
    }

    fun searchCommentators(str : String){
        viewModelScope.launch {
            commentatorRepo.searchCommentators(str).collectLatest {
                _foundCommentatorData.emit(Event.FoundCommentators(it))
                Log.d("view Model" , it.toString())
            }
        }
    }

    sealed class Event {
        data class Commentators(val commentators : ArrayList<CommentatorDto>) : Event()
        data class FoundCommentators(val foundCommentators : ArrayList<CommentatorDto>) : Event()
    }


}