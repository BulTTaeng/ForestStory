package com.greenstory.foreststory.view.fragment.audio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentAudioPlayerBinding
import com.greenstory.foreststory.databinding.FragmentMountainBinding
import com.greenstory.foreststory.model.audio.AudioDto
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.utility.DescriptionAdapter
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.AudioAdapter
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class AudioPlayerFragment : Fragment() {

    lateinit var binding: FragmentAudioPlayerBinding
    lateinit var audioPlayerActivity: AudioPlayerActivity
    lateinit var adapter : AudioAdapter

    private var player: ExoPlayer? = null

    var bitmap: Bitmap? = null
    lateinit var playerNotificationManager : PlayerNotificationManager

    val audioViewModel : AudioViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        audioPlayerActivity = context as AudioPlayerActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_audio_player, container, false)
        binding.fragment = this@AudioPlayerFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {

            var success = false

            CoroutineScope(Dispatchers.IO).launch {
                bitmap =
                    getBitmapFromURL("https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/abc.jpg?alt=media&token=2438536a-440d-4418-b022-3619ec387e09")
            }.join()

            success = audioViewModel.getAudioData("NSWzzdpkMgpn7ndD7WDQ")

            if(success) {
                initRecyclerView()
                initPlayerView()
            }
        }
    }

    fun initPlayerView(){

        player = ExoPlayer.Builder(audioPlayerActivity).build()
        binding.playerView.player = player
        binding.playerView.controllerShowTimeoutMs = 0

        player?.setMediaItems(audioViewModel.fetchAudioLinkData().value!!)
        player?.prepare()

        playerNotificationManager = PlayerNotificationManager.Builder(
            audioPlayerActivity,
            12, "ID"
        )
            .setChannelNameResourceId(R.string.id_name_empty)
            .setChannelDescriptionResourceId(R.string.try_later)
            .setMediaDescriptionAdapter(DescriptionAdapter(audioPlayerActivity, bitmap , audioViewModel.fetchAudioData().value!!))
            .build()

        playerNotificationManager.setPlayer(player)

        binding.progressBarAudio.visibility = View.GONE

    }


    fun initRecyclerView(){
        adapter = AudioAdapter()
        binding.playListRecyclerView.layoutManager = LinearLayoutManager(audioPlayerActivity)
        binding.playListRecyclerView.adapter = adapter
        observeData()
    }

    fun observeData(){

        var index = 0L

        audioViewModel.fetchAudioData().observe(
            viewLifecycleOwner, Observer {
                binding.progressBarAudio.visibility = View.VISIBLE



                adapter.submitList(it.map{
                    it.mapper(index++)
                }
                ).let {
                    binding.progressBarAudio.visibility = View.GONE
                }
            }
        )
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun AudioEntity.mapper(index : Long): AudioDto =
        AudioDto(id = index , link , audioName , commentator , likeNum , false)

}