package com.greenstory.foreststory.view.fragment.audio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentAudioPlayerBinding
import com.greenstory.foreststory.model.audio.AudioDto
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.model.audio.Audios
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.adapter.DescriptionAdapter
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.adapter.AudioAdapter
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    lateinit var playerNotificationManager : PlayerNotificationManager

    val audioViewModel : AudioViewModel by activityViewModels()


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

                            if (AudioPlayerFragment()::playerNotificationManager.isInitialized) {
                                playerNotificationManager.setPlayer(null)
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
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_audio_player, container, false)
        binding.fragment = this@AudioPlayerFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtDetailName.text = audioPlayerActivity.detailInfo?.name

        CoroutineScope(Dispatchers.Main).launch {

            CoroutineScope(Dispatchers.IO).launch {
                bitmap =
                    getBitmapFromURL(audioPlayerActivity.mountainInfo?.image)
            }.join()

            Glide.with(audioPlayerActivity).load(bitmap).into(binding.coverImageView)
            Glide.with(audioPlayerActivity).load(audioPlayerActivity.detailInfo?.image).into(binding.imgDetailLocation)
            binding.txtExplainDetail.text = audioPlayerActivity.detailInfo?.explain

            initPlayer()
            initRecyclerView()
            repeatOnStarted {
                audioViewModel.audioData.collectLatest { event ->
                    handleEvent(event)
                }
            }
        }

    }

    fun initPlayer(){
        player = ExoPlayer.Builder(audioPlayerActivity).build()
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

    fun getAudio(audios : Audios){
        if(audios.audioList.isEmpty()) {
            Toast.makeText(audioPlayerActivity , getString(R.string.no_audios) , Toast.LENGTH_SHORT).show()
            binding.progressBarAudio.visibility = View.GONE
            return
        }
        var index = 0L

        binding.progressBarAudio.visibility = View.VISIBLE

        adapter.submitList(audios.audioList.map {
            it.mapper(index++)
        })

        player?.setMediaItems(audios.audioLink)
        player?.prepare()

        playerNotificationManager = PlayerNotificationManager.Builder(
            audioPlayerActivity,
            12, "ID"
        )
            .setChannelNameResourceId(R.string.email)
            .setChannelDescriptionResourceId(R.string.password)
            .setMediaDescriptionAdapter(DescriptionAdapter(audioPlayerActivity, bitmap , audios.audioList))
            .build()


        playerNotificationManager.setPlayer(player)

            binding.currAudioDto = audios.audioList[0].mapper(0)


        binding.progressBarAudio.visibility = View.GONE
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

    fun updateTextInPlayer(audioName : String){
        binding.txtAudioNameInPlayer.text = audioName
    }

    fun AudioEntity.mapper(index : Long): AudioDto =
        AudioDto(id = index , link , audioName , commentator , likeNum , false)

    private fun handleEvent(event: AudioViewModel.Event) = when (event) {
        is AudioViewModel.Event.AudiosList -> getAudio(event.audios)
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