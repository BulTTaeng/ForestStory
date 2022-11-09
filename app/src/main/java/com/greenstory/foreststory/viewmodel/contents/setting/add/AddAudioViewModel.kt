package com.greenstory.foreststory.viewmodel.contents.setting.add

import androidx.lifecycle.ViewModel
import com.greenstory.foreststory.repository.contents.MountainRepository
import com.greenstory.foreststory.repository.contents.setting.add.AddAudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAudioViewModel @Inject constructor(val addAudioRepo: AddAudioRepository) : ViewModel() {
}