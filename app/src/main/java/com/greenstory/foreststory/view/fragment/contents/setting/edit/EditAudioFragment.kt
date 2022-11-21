package com.greenstory.foreststory.view.fragment.contents.setting.edit

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentEditAudioBinding
import com.greenstory.foreststory.model.audio.AudioDto
import com.greenstory.foreststory.model.audio.Audios
import com.greenstory.foreststory.model.audio.mapper
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.utility.recyclerview.SwipeHelper
import com.greenstory.foreststory.view.activity.contents.setting.add.AddAudioActivity
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.view.activity.contents.setting.edit.EditMyMountainActivity
import com.greenstory.foreststory.view.adapter.AudioEditAdapter
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.*


class EditAudioFragment : Fragment() {
    lateinit var binding: FragmentEditAudioBinding
    lateinit var adapter: AudioEditAdapter
    lateinit var editMyMountainActivity: EditMyMountainActivity
    val audioViewModel : AudioViewModel by activityViewModels()

    private var player: ExoPlayer? = null
    private val args by navArgs<EditAudioFragmentArgs>()

    lateinit var tempList : MutableList<AudioDto>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        editMyMountainActivity = context as EditMyMountainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
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

        //attachItemTouchHelper()
        attachSwipeHelper()
    }

    fun attachItemTouchHelper(){
        var itemTouchHelper = ItemTouchHelper( object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onMoved(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                fromPos: Int,
                target: RecyclerView.ViewHolder,
                toPos: Int,
                x: Int,
                y: Int
            ) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                val initial  = viewHolder.absoluteAdapterPosition
                val final =target.absoluteAdapterPosition

                tempList = adapter.currentList.toMutableList()

                Collections.swap(tempList , initial , final)
                adapter.submitList(tempList)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }


        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerAudio)

    }

    fun attachSwipeHelper(){
        object : SwipeHelper(editMyMountainActivity, binding.recyclerAudio, false) {

            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                // Archive Button
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                    "삭제",
                    AppCompatResources.getDrawable(editMyMountainActivity, R.drawable.delete_forever),
                    Color.WHITE, Color.RED) { pos: Int -> makeAlert(pos) })

                // Flag Button
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                    "수정",
                    AppCompatResources.getDrawable(editMyMountainActivity, R.drawable.edit),
                    Color.WHITE, Color.BLACK) { pos: Int -> editAudio(pos) })
            }

            override fun onMoved(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder,
                toPos: Int, x: Int, y: Int
            ) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                val initial = viewHolder.absoluteAdapterPosition
                val targetLoc = target.absoluteAdapterPosition
                tempList = ArrayList(adapter.currentList)
                Collections.swap(tempList, initial, targetLoc)
                adapter.submitList(tempList)
            }
        }

    }

    fun updateAudio(audios : Audios){
        if(audios.audioList.isEmpty()) {
            Toast.makeText(editMyMountainActivity , getString(R.string.no_audios) , Toast.LENGTH_SHORT).show()
            binding.progressBarEditAudio.visibility = View.GONE
            return
        }
        var index = 0L

        binding.progressBarEditAudio.visibility = View.VISIBLE

        adapter.submitList(audios.audioList.map {
            it.mapper()
        })

        player?.setMediaItems(audios.audioLink)
        player?.prepare()

        binding.currAudioDto = audios.audioList[0].mapper()


        binding.progressBarEditAudio.visibility = View.GONE
    }

    fun deleteAudio(pos : Int){

        player?.stop()
        audioViewModel.deleteAudio(editMyMountainActivity.name , args.detailInfo.name , adapter.currentList[pos].did)
    }

    fun editAudio(pos : Int){
        makeAlertWithEdtText(pos)
    }

    fun makeAlertWithEdtText( loc: Int) {
        val builder = AlertDialog.Builder(editMyMountainActivity)
        builder.setTitle("해설명 변경하기")

        // Set up the input
        val input = EditText(editMyMountainActivity)
        input.setText(adapter.currentList[loc].audioName)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton(
            "취소"
        ) { dialog, which -> dialog.cancel() }

        builder.setNegativeButton(
            "변경"
        ) { _, which -> audioViewModel.editAudioName(loc , editMyMountainActivity.name ,args.detailInfo.name , adapter.currentList[loc].did  , input.text.toString()) }

        builder.show()
    }

    fun changeAudioName(loc : Int, str : String){
        adapter.currentList[loc].audioName = str
        adapter.notifyItemChanged(loc)
    }

    fun makeAlert(pos : Int){
        AlertDialog.Builder(editMyMountainActivity)
            .setTitle("정말로 삭제하시겠습니까")
            .setPositiveButton("아니요") { _: DialogInterface, i: Int ->
            }
            .setNegativeButton("네") { _: DialogInterface, i: Int ->
                deleteAudio(pos)
            }.show()
    }

    fun btnAddAudio(view: View){
        val intent = Intent(editMyMountainActivity , AddAudioActivity::class.java)
        intent.putExtra("MOUNTAINNAME" , editMyMountainActivity.name)
        intent.putExtra("DETAILINFO" , args.detailInfo)
        intent.putExtra("SIZE" , adapter.currentList.size.toLong())
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
        is AudioViewModel.Event.AudiosList -> updateAudio(event.audios)
        is AudioViewModel.Event.EditedLoc -> changeAudioName(event.locString.first , event.locString.second)
        is AudioViewModel.Event.Success -> audioViewModel.getAudioData(editMyMountainActivity.name , args.detailInfo.name)
    }

    companion object{
        const val PROGRAM_EDITED: Int = 777
        const val AUDIO_EDITED : Int = 776
    }


}