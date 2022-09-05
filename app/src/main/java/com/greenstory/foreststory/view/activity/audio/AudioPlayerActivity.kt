package com.greenstory.foreststory.view.activity.audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.greenstory.foreststory.databinding.ActivityAudioPlayerBinding
import com.greenstory.foreststory.view.fragment.audio.AudioPlayerFragment
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity() {

    private var _viewBinding: ActivityAudioPlayerBinding? = null
    private val binding: ActivityAudioPlayerBinding get() = requireNotNull(_viewBinding)
    val audioViewModel : AudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.frameAudioPlayer.id, AudioPlayerFragment())
            .commit()

    }
}