package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentChangeProfileBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel


class ChangeProfileFragment : Fragment() {

    lateinit var binding: FragmentChangeProfileBinding
    val settingViewModel: SettingViewModel by activityViewModels()
    val commentatorViewModel : CommentatorViewModel by activityViewModels()
    lateinit var contentsActivity: ContentsActivity
    lateinit var  pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    var hashTagIsComing = 0
    var endChar = ' '
    var str =""

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

        settingViewModel.getFullUserInfo()


        repeatOnStarted {
            settingViewModel.myInfo.collect() { event ->
                handleEventSetting(event)
            }
        }

        repeatOnStarted {
            commentatorViewModel.commentatorData.collect() { event ->
                handleEventCommentator(event)
            }
        }

        edtAddListener()
    }

    private fun edtAddListener(){
        binding.edtChangeHashTag.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var startChar: String? = null

                try {
                    startChar = s[start].toString()
                } catch (ex: Exception) {
                    startChar = ""
                }

                if (startChar == " ") {
                    if(hashTagIsComing <3) {
                        str += "#"
                        binding.edtChangeHashTag.setText(str)
                        binding.edtChangeHashTag.setSelection(binding.edtChangeHashTag.length())
                        hashTagIsComing++
                    }
                    else{
                        Toast.makeText(contentsActivity,  "해시태그는 3개 까지 입력 가능합니다" , Toast.LENGTH_SHORT).show()
                        binding.edtChangeHashTag.setText(str)
                        binding.edtChangeHashTag.clearFocus()
                    }
                }
                else if(count == 0){ //back pressed

                    if(str[start] == '#') {
                        hashTagIsComing--
                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                str = binding.edtChangeHashTag.text.toString()
                endChar = str.last()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun btnBackButtonInChangeProfile(view: View){
        findNavController().navigate(R.id.settingFragment)
    }

    fun btnChangeProfileImage(view: View){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun btnUpdateProfile(view: View){

        if(binding.edtChangeName.text.isEmpty()){
            Toast.makeText(contentsActivity , "닉네임은 비워 둘 수 없습니다" , Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBarChangeProfile.visibility = View.VISIBLE
        settingViewModel.updateProfile(
            binding.edtChangeName.text.toString()
        )


    }

    private fun getCommentatorInfo(info : CommentatorDto?) {
        Log.d("wwwwwwwww" , info.toString())
        binding.edtChangeName.setText(info?.name)
        binding.edtChangeNickName.setText(info?.nickName)
        binding.edtChangeExplain.setText(info?.explain)
        if(info?.hashTag != null) {
            changeToHashTage(info.hashTag)
        }

//        binding.txtUserEmailMyPage.text = info[1]
        info?.profile.toString()

        Glide.with(contentsActivity).load(info?.profile.toString()).into(binding.imgCommentatorImageInChange)

        binding.progressBarChangeProfile.visibility = View.GONE
    }

    private fun getUserInfo(fullInfo : UserInfoEntity) {

        if(fullInfo.admin){
            commentatorViewModel.getMyCommentator()
        }
        else {
            binding.edtChangeName.setText(fullInfo.name)
            binding.edtChangeNickName.setText(fullInfo.nickName)
            binding.edtChangeExplain.setText(fullInfo.explain)
            Glide.with(contentsActivity).load(fullInfo.profile)
                .into(binding.imgCommentatorImageInChange)

            binding.edtChangeHashTag.visibility = View.GONE
            binding.txtChangeHashTag.visibility = View.GONE

            binding.progressBarChangeProfile.visibility = View.GONE
        }
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

    private fun handleEventSetting(event: SettingViewModel.Event) = when (event) {
        is SettingViewModel.Event.UpdateInfo -> findNavController().navigate(R.id.settingFragment)
        is SettingViewModel.Event.FullUserInfo -> getUserInfo(event.fullInfo)
        else ->{}
    }

    private fun handleEventCommentator(event: CommentatorViewModel.Event) = when (event) {
        is CommentatorViewModel.Event.OneCommentator -> getCommentatorInfo(event.oneCommentator)
        else ->{}
    }

    private fun changeToHashTage(str : String){
        var hashTags = ""

        for(it in str.split(" ")){
            hashTags += "#$it"
            hashTagIsComing++
        }


        if(hashTags.isEmpty()){
            hashTags = "#"
            hashTagIsComing =1
        }
        binding.edtChangeHashTag.setText(hashTags)
    }




}