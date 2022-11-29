package com.greenstory.foreststory.view.fragment.audio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentAudioPlayerBinding
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.model.audio.Audios
import com.greenstory.foreststory.model.audio.mapper
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.adapter.AudioAdapter
import com.greenstory.foreststory.view.adapter.DescriptionAdapter
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class AudioPlayerFragment : Fragment() {

    lateinit var binding: FragmentAudioPlayerBinding
    lateinit var audioPlayerActivity: AudioPlayerActivity
    lateinit var adapter : AudioAdapter
    private var backPressedTime : Long = 0

    private var player: ExoPlayer? = null

    var bitmap: Bitmap? = null
    var playerNotificationManager : PlayerNotificationManager? = null

    val audioViewModel : AudioViewModel by activityViewModels()

    var audioLists = ArrayList<AudioEntity>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        audioPlayerActivity = context as AudioPlayerActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if(binding.playListGroup.isVisible){
                        if(System.currentTimeMillis() - backPressedTime < 2500){

                            if (playerNotificationManager != null) {
                                playerNotificationManager?.setPlayer(null)
                            }
                            player?.release()
                            audioPlayerActivity.finish()

                            return  // 로직 종료(토스트 메시지 안 띄우기 위해)
                        }
                        Toast.makeText(audioPlayerActivity, getText(R.string.doubletap_to_exit), Toast.LENGTH_SHORT).show()
                        backPressedTime = System.currentTimeMillis()
                    }
                    else{
                        binding.playerViewGroup.visibility = View.GONE
                        binding.playListGroup.visibility = View.VISIBLE
                    }
                }
            }
        audioPlayerActivity.onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_audio_player, container, false)
        binding.fragment = this@AudioPlayerFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtDetailName.text = audioPlayerActivity.detailInfo?.name
        binding.txtExplainDetail.text = audioPlayerActivity.detailInfo?.explain

        audioViewModel.getBitmapFromURL(audioPlayerActivity.mountainInfo?.image)

        initPlayer()
        initRecyclerView()
        repeatOnStarted {
            audioViewModel.audioData.collectLatest { event ->
                handleEvent(event)
            }
        }
    }



    fun initPlayer(){
        player = ExoPlayer.Builder(audioPlayerActivity).build()
        player?.addListener(object : Player.Listener{

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                var loc = 0

                if(player != null){
                    loc = player!!.currentMediaItemIndex
                }

                if(adapter.currLoc != loc.toLong()) {
                    if (isPlaying) {
                        adapter.currentList[loc].isPlaying = true
                        adapter.currLoc = loc.toLong()
                        adapter.notifyItemChanged(loc)
                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)

                var loc = 0

                if(player != null){
                    loc = player!!.currentMediaItemIndex
                }

                if (adapter.currLoc != -1L) {
                    adapter.currentList[adapter.currLoc.toInt()].isPlaying = false
                    adapter.notifyItemChanged(adapter.currLoc.toInt())

                    adapter.currentList[loc].isPlaying = true
                    adapter.notifyItemChanged(loc)

                    adapter.currLoc = loc.toLong()
                    // 리사이클러 뷰 스크롤 이동
                    binding.recyclerPlayList.scrollToPosition(adapter.currLoc.toInt())

                    updateTextInPlayer(adapter.currentList[loc].audioName)
                }
            }

        })
        binding.playerView.player = player
        binding.playerView.controllerShowTimeoutMs = 0
        binding.playerView.controllerHideOnTouch = false

    }


    fun initRecyclerView(){
        adapter = AudioAdapter(player!! , this)
        binding.recyclerPlayList.layoutManager = LinearLayoutManager(audioPlayerActivity)
        binding.recyclerPlayList.adapter = adapter
        binding.recyclerPlayList.isNestedScrollingEnabled = false
    }

    fun updateAudio(audios : Audios){
        if(audios.audioList.isEmpty()) {
            Toast.makeText(audioPlayerActivity , getString(R.string.no_audios) , Toast.LENGTH_SHORT).show()
            binding.progressBarAudio.visibility = View.GONE
            return
        }

        binding.progressBarAudio.visibility = View.VISIBLE

        adapter.submitList(audios.audioList.map {
            it.mapper()
        })

        player?.setMediaItems(audios.audioLink)
        player?.prepare()

        audioLists = audios.audioList

        binding.currAudioDto = audios.audioList[0].mapper()


        binding.progressBarAudio.visibility = View.GONE
    }

    fun updateBitmapImage(bitmapImage: Bitmap?){
        bitmap = bitmapImage

        initNotification()

        Glide.with(audioPlayerActivity).load(bitmap).into(binding.coverImageView)
        Glide.with(audioPlayerActivity).load(audioPlayerActivity.detailInfo?.image).into(binding.imgDetailLocation)
    }

    fun initNotification(){
        playerNotificationManager = PlayerNotificationManager.Builder(
            audioPlayerActivity,
            12, "ID"
        ).setChannelNameResourceId(R.string.idd)
            .setChannelDescriptionResourceId(R.string.pass)
            .setMediaDescriptionAdapter(DescriptionAdapter(audioPlayerActivity, bitmap , audioLists))
            .build()


        playerNotificationManager?.setPlayer(player)
    }

    fun updateTextInPlayer(audioName : String){
        binding.txtAudioNameInPlayer.text = audioName
    }

    private fun handleEvent(event: AudioViewModel.Event) = when (event) {
        is AudioViewModel.Event.AudiosList -> updateAudio(event.audios)
        is AudioViewModel.Event.BitmapImage -> updateBitmapImage(event.bitmap)
        else ->{}
    }

    fun btnShowCoverImage(view: View){
        if(binding.playListGroup.isVisible){
            var loc = adapter.currLoc.toInt()
            if(loc == -1 ){
                loc = 0
            }
            binding.currAudioDto = adapter.currentList[loc]
            //디테일 페이지 보여주기 전에 이미지 바꾸려면 여기서 하면됨
            //Glide.with(audioPlayerActivity).load(adapter.currentList[loc].)
            binding.playListGroup.visibility = View.GONE
            binding.playerViewGroup.visibility = View.VISIBLE
        }
    }

    fun btnShowList(view: View){
        binding.playerViewGroup.visibility = View.GONE
        binding.playListGroup.visibility = View.VISIBLE
    }

    fun btnShowOptions(view: View){
        val themeWrapper = ContextThemeWrapper(context , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper , binding.btnShowOptions, Gravity.END , 0 , R.style.MyPopupMenu)

        popupMenu.inflate(R.menu.show_options_audio)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.report -> {
                        return true
                    }
                    R.id.block -> {
                        return true
                    }
                    R.id.share ->{
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }


}