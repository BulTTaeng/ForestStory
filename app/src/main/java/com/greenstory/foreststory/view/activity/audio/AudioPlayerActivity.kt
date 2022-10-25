package com.greenstory.foreststory.view.activity.audio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityAudioPlayerBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.fragment.audio.AudioPlayerFragment
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity() {

    private var _viewBinding: ActivityAudioPlayerBinding? = null
    private val binding: ActivityAudioPlayerBinding get() = requireNotNull(_viewBinding)
    val audioViewModel : AudioViewModel by viewModels()
    var mountainInfo : MountainDto? = null
    var commentatorDto : CommentatorDto? = CommentatorDto()
    var detailInfo : DetailLocationInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as GlobalApplication).currContext = this

        mountainInfo = intent.getParcelableExtra<MountainDto>("MOUNTAIN")
        detailInfo = intent.getParcelableExtra<DetailLocationInfo>("DETAIL")
        //commentatorDto = intent.getParcelableExtra<CommentatorDto>("COMMENTATORINFO")


        getViewModelData(mountainInfo?.name , detailInfo?.name)
    }

    private fun getViewModelData(name : String? , detailName : String?){

        audioViewModel.getAudioData(name!! , detailName!!)

        supportFragmentManager.beginTransaction()
            .replace(binding.frameAudioPlayer.id, AudioPlayerFragment())
            .commit()
    }
}