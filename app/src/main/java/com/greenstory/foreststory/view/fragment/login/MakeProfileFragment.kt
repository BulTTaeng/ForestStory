package com.greenstory.foreststory.view.fragment.login

import android.content.Context
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentMakeProfileBinding
import com.greenstory.foreststory.databinding.FragmentSignUpBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.viewmodel.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log


class MakeProfileFragment : Fragment() {

    lateinit var binding: FragmentMakeProfileBinding
    lateinit var loginActivity: LoginActivity
    val loginViewModel: LoginViewModel by activityViewModels()
    var  pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_make_profile, container, false)
        binding.fragment = this@MakeProfileFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(loginActivity).load(loginViewModel.userInfo.profile).into(binding.imgUserProfileImage)
        registerPhotoPicker()
    }

    fun registerPhotoPicker(){

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                loginViewModel.userInfo.profile = uri.toString()
                Glide.with(loginActivity).load(loginViewModel.userInfo.profile).into(binding.imgUserProfileImage)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    fun btnUploadProfileImage(view: View){
        pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun btnDoneSignUp(view: View){

        loginViewModel.userInfo.name = binding.edtName.text.toString()
        loginViewModel.userInfo.nickName = binding.edtNickName.text.toString()
        loginViewModel.userInfo.explain = binding.edtFirstExplain.text.toString()


        if (binding.edtName.text == null || binding.edtName.text.toString() == "") {
            Toast.makeText(loginActivity, "이름을 입력해 주세요", Toast.LENGTH_LONG).show()
        } else if (binding.edtNickName.text == null || binding.edtNickName.text.toString() == "") {
            Toast.makeText(loginActivity, "닉네임을 확인해 주세요", Toast.LENGTH_LONG).show()
        } else{
            binding.progressBarMakeProfile.visibility =View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                val success =
                    loginViewModel.emailSignUp(loginViewModel.userInfo, loginViewModel.password)
                goToContentsActivity(success)
            }
        }
    }

    private fun goToContentsActivity(success : Boolean){
        if (success) {
            val intent = Intent(loginActivity, ContentsActivity::class.java)
            startActivity(intent)
            loginActivity.finish()
        } else {
            Toast.makeText(loginActivity, getText(R.string.try_later), Toast.LENGTH_LONG)
                .show()
        }
        binding.progressBarMakeProfile.visibility =View.GONE
    }
}