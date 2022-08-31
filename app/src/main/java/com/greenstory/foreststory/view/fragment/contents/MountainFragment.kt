package com.greenstory.foreststory.view.fragment.contents

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentMountainBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MountainFragment : Fragment() {
    lateinit var binding : FragmentMountainBinding
    lateinit var contentsActivity: ContentsActivity
    lateinit var  mediaPlayer : MediaPlayer
    val timeFormat = SimpleDateFormat("mm:ss")
    var isReady = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentsActivity = context as ContentsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayer = MediaPlayer().apply {
            setWakeMode(contentsActivity, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource("https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/BIG%20Naughty%20%EC%84%9C%EB%8F%99%ED%98%84%20%20Vancouver.mp3?alt=media&token=04fbbc2a-9a70-4169-9743-33c059a4a1eb")
        }
        mediaPlayer.setOnPreparedListener {
            isReady = true
            it.isLooping = false
            it.start()
            startSeekBarThread()
        }
        mediaPlayer.prepareAsync()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mountain, container, false)
        binding.fragment = this@MountainFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        isReady = false
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
    }

    fun play(view: View){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
        else{
            if(isReady) {
                mediaPlayer.start()
                startSeekBarThread()
            }
            /* 실시간으로 변경되는 진행시간과 시크바를 구현하기 위한 스레드 사용*/

        }

    }

    fun startSeekBarThread(){

        binding.seekbar.max = mediaPlayer.duration  // mPlayer.duration : 음악 총 시간

        CoroutineScope(Dispatchers.Main).launch {
            while(mediaPlayer.isPlaying){
                binding.seekbar.progress = mediaPlayer.currentPosition
                //binding.time.text = "진행시간 : " + timeFormat.format(mediaPlayer.currentPosition) + "/" + timeFormat.format(mediaPlayer.duration)
                binding.time.text = timeFormat.format(mediaPlayer.currentPosition)
                delay(250)
            }
        }

//        object : Thread() {
//            override fun run() {
//                super.run()
//                while (mediaPlayer.isPlaying) {
//                    contentsActivity.runOnUiThread { //화면의 위젯을 변경할 때 사용 (이 메소드 없이 아래 코드를 추가하면 실행x)
//                        binding.seekbar.progress = mediaPlayer.currentPosition
//                        binding.time.text = "진행시간 : " + timeFormat.format(mediaPlayer.currentPosition) + "/" + timeFormat.format(mediaPlayer.duration)
//                    }
//                    SystemClock.sleep(500)
//                }
//
////                        /*1. 음악이 종료되면 자동으로 초기상태로 전환*/
////                        /*btnStop.setOnClickListener()와 동일한 코드*/
////                        if(!mediaPlayer.isPlaying){
////                            mediaPlayer.stop()      //음악 정지
////                            mediaPlayer.reset()
////                            binding.time.text = "실행중인 음악 : "
////                            binding.seekbar.progress = 0
////                            binding.time.text = "진행시간 : "
////                        }
//            }
//        }.start()
    }
}