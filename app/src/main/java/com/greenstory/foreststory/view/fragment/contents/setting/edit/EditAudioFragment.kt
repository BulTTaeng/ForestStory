package com.greenstory.foreststory.view.fragment.contents.setting.edit

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentEditAudioBinding
import com.greenstory.foreststory.model.audio.Audios
import com.greenstory.foreststory.model.audio.mapper
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.setting.add.AddAudioActivity
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.view.activity.contents.setting.edit.EditMyMountainActivity
import com.greenstory.foreststory.view.adapter.AudioEditAdapter
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import kotlinx.coroutines.flow.collectLatest


class EditAudioFragment : Fragment() {
    lateinit var binding: FragmentEditAudioBinding
    lateinit var adapter: AudioEditAdapter
    lateinit var editMyMountainActivity: EditMyMountainActivity
    private var backPressedTime : Long = 0

    val audioViewModel : AudioViewModel by activityViewModels()
    private var player: ExoPlayer? = null

    private val args by navArgs<EditAudioFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        editMyMountainActivity = context as EditMyMountainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                        if(System.currentTimeMillis() - backPressedTime < 2500){
                            player?.release()
                            editMyMountainActivity.onBackPressed()
                            return
                        }
                        Toast.makeText(editMyMountainActivity, getText(R.string.doubletap_to_exit), Toast.LENGTH_SHORT).show()
                        backPressedTime = System.currentTimeMillis()

                }
            }
        editMyMountainActivity.onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_audio, container, false)
        binding.fragment = this@EditAudioFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locations = args.detailInfo
        Glide.with(editMyMountainActivity).load(args.detailInfo.image).into(binding.imgDetailLocation)
        initPlayer()
        initRecyclerView()
        audioViewModel.getAudioData(editMyMountainActivity.name , args.detailInfo.name)
        repeatOnStarted {
            audioViewModel.audioData.collectLatest { event ->
                handleEvent(event)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == PROGRAM_EDITED ){
            //UI 업데이트
            val editedDetailInfo = data?.getParcelableExtra<DetailLocationInfo>("EDITEDINFO")
            binding.locations = editedDetailInfo
        }
        else if(resultCode == RESULT_OK && requestCode == AUDIO_EDITED ){
            val edited = data?.getStringExtra("EDITED").toString()
            if(edited == "edited") {
                audioViewModel.getAudioData(editMyMountainActivity.name, args.detailInfo.name)
            }
        }
    }

    fun initPlayer(){
        player = ExoPlayer.Builder(editMyMountainActivity).build()
        binding.playerView.player = player
        binding.playerView.controllerShowTimeoutMs = 0
        binding.playerView.controllerHideOnTouch = false

    }

    fun initRecyclerView(){
        adapter = AudioEditAdapter(player!!)
        binding.recyclerAudio.layoutManager = LinearLayoutManager(editMyMountainActivity)
        binding.recyclerAudio.adapter = adapter
        binding.recyclerAudio.isNestedScrollingEnabled = false
    }

    fun getAudio(audios : Audios){
        if(audios.audioList.isEmpty()) {
            Toast.makeText(editMyMountainActivity , getString(R.string.no_audios) , Toast.LENGTH_SHORT).show()
            binding.progressBarEditAudio.visibility = View.GONE
            return
        }
        var index = 0L

        binding.progressBarEditAudio.visibility = View.VISIBLE

        adapter.submitList(audios.audioList.map {
            it.mapper(index++)
        })

        player?.setMediaItems(audios.audioLink)
        player?.prepare()

        binding.currAudioDto = audios.audioList[0].mapper(0)


        binding.progressBarEditAudio.visibility = View.GONE
    }

    fun btnAddAudio(view: View){
        val intent = Intent(editMyMountainActivity , AddAudioActivity::class.java)
        intent.putExtra("MOUNTAINNAME" , editMyMountainActivity.name)
        intent.putExtra("DETAILINFO" , args.detailInfo)
        startActivityForResult(intent ,AUDIO_EDITED )
    }

    fun btnBackButtonInEditAudio(view: View){
        editMyMountainActivity.onBackPressed()
    }

    fun btnEditProgram(view: View){
        val popupMenu = PopupMenu(editMyMountainActivity , binding.btnEditProgram)

        popupMenu.inflate(R.menu.show_options_edit)
        popupMenu.show()


        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.edit -> {
                        val intent = Intent(editMyMountainActivity , AddMountainProgramActivity::class.java)
                        intent.putExtra("MOUNTAINNAME" , editMyMountainActivity.name)
                        intent.putExtra("DETAILINFO",args.detailInfo)
                        startActivityForResult(intent , PROGRAM_EDITED)
                    }
                    R.id.delete -> {
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    private fun handleEvent(event: AudioViewModel.Event) = when (event) {
        is AudioViewModel.Event.AudiosList -> getAudio(event.audios)
    }

    companion object{
        const val PROGRAM_EDITED: Int = 777
        const val AUDIO_EDITED : Int = 776
    }


}