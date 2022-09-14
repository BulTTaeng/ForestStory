package com.greenstory.foreststory.view.activity.audio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityAudioPlayerBinding
import com.greenstory.foreststory.utility.GlobalApplication
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as GlobalApplication).currContext = this

        val name = intent.getStringExtra("MOUNTAINNAME")

        getViewModelData(name)
    }

    fun getViewModelData(name : String?){
        CoroutineScope(Dispatchers.Main).launch {

            audioViewModel.getAudioData("NSWzzdpkMgpn7ndD7WDQ")

            supportFragmentManager.beginTransaction()
                .replace(binding.frameAudioPlayer.id, AudioPlayerFragment())
                .commit()
        }
    }

    fun getC(): AudioPlayerActivity {
        return this
    }
}