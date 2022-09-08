package com.greenstory.foreststory.view.fragment.contents

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentMountainBinding
import com.greenstory.foreststory.model.audio.AudioDto
import com.greenstory.foreststory.utility.DescriptionAdapter
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MountainFragment : Fragment() {
    lateinit var binding: FragmentMountainBinding
    lateinit var contentsActivity: ContentsActivity
    private lateinit var callback: OnBackPressedCallback

    private var player: ExoPlayer? = null

    private val playWhenReady = true
    private val currentWindow = 0
    private val playbackPosition = 0L
    private val songUrl: String =
        "https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/BIG%20Naughty%20%EC%84%9C%EB%8F%99%ED%98%84%20%20Vancouver.mp3?alt=media&token=04fbbc2a-9a70-4169-9743-33c059a4a1eb"
    var bitmap: Bitmap? = null
    var playerNotificationManager : PlayerNotificationManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentsActivity = context as ContentsActivity
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.playerView.player = null
                player?.release()
                player = null
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mountain, container, false)
        binding.fragment = this@MountainFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).launch {
                bitmap =
                    getBitmapFromURL("https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/abc.jpg?alt=media&token=2438536a-440d-4418-b022-3619ec387e09")
            }.join()

            //initializePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerNotificationManager?.setPlayer(null)
        player?.release()
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
    } // Author: silentnuke


    private fun initializePlayer() {
        if (player == null) {

            player = ExoPlayer.Builder(contentsActivity).build()
            player?.playWhenReady = true
            binding.playerView.player = player
            binding.playerView.controllerShowTimeoutMs = 0
            val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            val mediaItem =
                MediaItem.fromUri(Uri.parse(songUrl))
            player?.setMediaItem(mediaItem)
//            val mediaSource =
//                HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
//            player?.setMediaSource(mediaSource)
            player?.seekTo(playbackPosition)
            player?.playWhenReady = playWhenReady
            player?.prepare()
            player!!.setForegroundMode(true)
            binding.playerView.controllerShowTimeoutMs = 0
            binding.playerView.setShowMultiWindowTimeBar(true)

            playerNotificationManager?.setPlayer(player)

        }
    }

    fun play(view: View) {

    }

    fun tempB(view: View){
        val intent = Intent(contentsActivity , AudioPlayerActivity::class.java)
        startActivity(intent)
    }

}