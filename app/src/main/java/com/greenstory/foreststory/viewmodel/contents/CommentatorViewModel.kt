package com.greenstory.foreststory.viewmodel.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.repository.contents.CommentatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CommentatorViewModel @Inject constructor(val commentatorRepo : CommentatorRepository) : ViewModel() {

    val _mutableCommentatorData = MutableLiveData<MutableList<CommentatorDto>>()
    val commentatorData : LiveData<MutableList<CommentatorDto>>
        get() =_mutableCommentatorData

    fun getCommentators(){
        viewModelScope.launch {
            commentatorRepo.getCommentators().collectLatest() {
                _mutableCommentatorData.value = it
            }
        }
    }
}