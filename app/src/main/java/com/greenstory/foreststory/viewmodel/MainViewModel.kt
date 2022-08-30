package com.greenstory.foreststory.viewmodel

import androidx.lifecycle.ViewModel
import com.greenstory.foreststory.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val mainRepo : MainRepository) : ViewModel() {

    fun isLogin() : Int{
        return mainRepo.isLogin()
    }
}