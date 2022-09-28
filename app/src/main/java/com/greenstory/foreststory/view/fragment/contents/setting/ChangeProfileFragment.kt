package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentChangeProfileBinding
import com.greenstory.foreststory.databinding.FragmentSettingBinding
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel


class ChangeProfileFragment : Fragment() {

    lateinit var binding: FragmentChangeProfileBinding
    val settingViewModel: SettingViewModel by activityViewModels()
    lateinit var contentsActivity: ContentsActivity
    lateinit var  pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentsActivity = context as ContentsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_profile, container, false)
        binding.fragment = this@ChangeProfileFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarChangeProfile.visibility = View.GONE
        registerPhotoPicker()

        settingViewModel.getUserNameAndEmailProfileImage()


        repeatOnStarted {
            settingViewModel.myInfo.collect() { event ->
                handleEvent(event)
            }
        }

        binding.imgCommentatorImageInChange
    }

    fun btnBackButtonInChangeProfile(view: View){
        findNavController().navigate(R.id.settingFragment)
    }

    fun btnChangeProfileImage(view: View){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun btnUpdateProfile(view: View){

        if(binding.edtNickNameChange.text.isEmpty()){
            Toast.makeText(contentsActivity , "닉네임은 비워 둘 수 없습니다" , Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBarChangeProfile.visibility = View.VISIBLE
        settingViewModel.updateProfile(
            binding.edtNickNameChange.text.toString()
        )


    }

    fun getUserInfo(info : ArrayList<String>) {
        var profileImage = ""
        binding.edtNickNameChange.setText(info[0])
//        binding.txtUserEmailMyPage.text = info[1]
        profileImage = info[2]

        Glide.with(contentsActivity).load(profileImage).into(binding.imgCommentatorImageInChange)

        binding.progressBarChangeProfile.visibility = View.GONE
    }

    fun registerPhotoPicker(){

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                settingViewModel.selectedImageUri = uri
                Glide.with(contentsActivity).load(settingViewModel.selectedImageUri).into(binding.imgCommentatorImageInChange)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    private fun handleEvent(event: SettingViewModel.Event) = when (event) {
        is SettingViewModel.Event.Info -> getUserInfo(event.info)
        is SettingViewModel.Event.UpdateInfo -> findNavController().navigate(R.id.settingFragment)
    }


}