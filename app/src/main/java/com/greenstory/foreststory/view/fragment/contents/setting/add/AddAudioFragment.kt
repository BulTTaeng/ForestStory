package com.greenstory.foreststory.view.fragment.contents.setting.add

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentAddAudioBinding
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.setting.add.AddAudioActivity
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.add.AddAudioViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.collectLatest
import java.io.Serializable


class AddAudioFragment : Fragment() {

    lateinit var binding: FragmentAddAudioBinding
    lateinit var addAudioActivity: AddAudioActivity
    val addAudioViewModel : AddAudioViewModel by activityViewModels()

    val selectAudioResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data.let {
                addAudioViewModel.audioString = it?.data.toString()
                Glide.with(addAudioActivity).load(R.drawable.uploaded_audio).into(binding.btnUploadAudio)
                binding.txtUploadedAudio.text = getFileName(Uri.parse(it?.data?.toString()))
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addAudioActivity = context as AddAudioActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_audio, container, false)
        binding.fragment = this@AddAudioFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnStarted {
            addAudioViewModel.addAudioEvent.collectLatest { event ->
                handleEvent(event)
            }
        }
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.getScheme().equals("content")) {
            val cursor: Cursor? = addAudioActivity.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }

    fun finishIfSuccess(success : Boolean){
        if(success){
            val intent = Intent()
            intent.putExtra("EDITED" , "edited")
            addAudioActivity.setResult(Activity.RESULT_OK , intent)
            addAudioActivity.finish()
        }
        else{
            Toast.makeText(addAudioActivity , getString(R.string.try_later) , Toast.LENGTH_SHORT).show()
            binding.progressBarAddAudio.visibility = View.GONE
            binding.btnUploadAudioToFirebase.isClickable = true
            binding.btnUploadAudioToFirebase.isEnabled=true
        }

    }

    fun btnUploadAudio(view: View){
        val audio = Intent()
        audio.type = "audio/*"
        audio.action = Intent.ACTION_GET_CONTENT

        selectAudioResult.launch(Intent.createChooser(audio, "Select Audio"))
    }

    fun btnUploadAudioToFirebase(view: View){

        if(binding.edtAudioNameInAdd.text.isEmpty()){
            Toast.makeText(addAudioActivity , "숲해설 제목을 입력해 주세요" , Toast.LENGTH_SHORT).show()
            return
        }
        else if(binding.txtUploadedAudio.text.isEmpty()){
            Toast.makeText(addAudioActivity , "오디오 파일을 선택해 주세요" , Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBarAddAudio.visibility = View.VISIBLE
        binding.btnUploadAudioToFirebase.isClickable = false
        binding.btnUploadAudioToFirebase.isEnabled=false
        val audioEntity = AudioEntity()
        audioEntity.sequence = addAudioActivity.size
        audioEntity.audioName = binding.edtAudioNameInAdd.text.toString()
        audioEntity.commentator = addAudioViewModel.detailInfo.commentator


        addAudioViewModel.upLoadAudio(audioEntity)
    }

    fun handleEvent(event: AddAudioViewModel.Event) = when (event) {
        is AddAudioViewModel.Event.Success -> finishIfSuccess(event.success)
        else ->{}
    }

    companion object{
        const val PICK_AUDIO = 987
    }

}