package com.greenstory.foreststory.viewmodel.contents

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.UserInfo
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.model.contents.CommentatorEntity
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.repository.contents.CommentatorRepository
import com.greenstory.foreststory.utility.event.MutableEventFlow
import com.greenstory.foreststory.utility.event.asEventFlow
import com.kakao.usermgmt.response.model.User
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

    var userInfo : UserInfoEntity? = null

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
                _commentatorData.emit(Event.FoundCommentators(it))
            }
        }
    }

    fun getUserInfo(){
        viewModelScope.launch {
            commentatorRepo.getUserInfo().collectLatest {
                userInfo = it
            }
        }
    }

    fun getMyCommentator(){
        viewModelScope.launch {
            commentatorRepo.getMyCommentator().collectLatest() {
                _commentatorData.emit(Event.OneCommentator(it))
            }
        }
    }

    fun getFollowCommentator(){
        viewModelScope.launch {
            commentatorRepo.getFollowCommentator().collectLatest() {
                _commentatorData.emit(Event.FoundCommentators(it))
            }
        }
    }

    fun stopFollow(id : String){
        viewModelScope.launch {
            commentatorRepo.stopFollow(id).collectLatest() {
                _commentatorData.emit(Event.Success(it))
            }
        }
    }

    fun getMyAudioImages(programNames :ArrayList<String>){
        viewModelScope.launch {
            commentatorRepo.getMyCommentator().collectLatest() {
                _commentatorData.emit(Event.OneCommentator(it))
            }
        }
    }

    sealed class Event {
        data class Commentators(val commentators : ArrayList<CommentatorDto>) : Event()
        data class FoundCommentators(val foundCommentators : ArrayList<CommentatorDto>) : Event()
        data class OneCommentator(val oneCommentator: CommentatorDto?) : Event()
        data class Success(val success : Boolean) : Event()
    }


}