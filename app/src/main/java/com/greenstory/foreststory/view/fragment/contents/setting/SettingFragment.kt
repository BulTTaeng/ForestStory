package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentSettingBinding
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.view.adapter.SettingAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class SettingFragment : Fragment() {

    val settingViewModel: SettingViewModel by activityViewModels()
    lateinit var binding: FragmentSettingBinding
    lateinit var contentsActivity: ContentsActivity
    lateinit var adapter: SettingAdapter
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var callback: OnBackPressedCallback

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.fragment = this@SettingFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarSetting.visibility = View.GONE
        googleSignInClient = settingViewModel.getGoogleSignInClient(contentsActivity)

        settingViewModel.getUserNameAndEmailProfileImage()
        initRecyclerView()
        repeatOnStarted {
            settingViewModel.myInfo.collect() { event ->
                handleEvent(event)
            }
        }
    }



    fun initRecyclerView() {
        adapter = SettingAdapter()
        adapter.setOnClickListener(
            listOf(
                "자격증 인증",
                "관심 목록",
                "프로필 변경",
                "내 오디오 보기",
                "로그아웃",
                "회원 탈퇴"
            )
        ) {
            when (it) {
                "자격증 인증" -> Log.d("111", "111")
                "관심 목록" -> Log.d("222", "222")
                "프로필 변경" -> toChangeProfile()
                "내 오디오 보기" -> Log.d("444", "$444")
                "로그아웃" -> logOut()
                "회원 탈퇴" -> withDraw()
            }
        }
        binding.recyclerSetting.layoutManager = LinearLayoutManager(contentsActivity)
        binding.recyclerSetting.adapter = adapter
    }

    fun getUserInfo(info : ArrayList<String>) {
        var profileImage = ""
        binding.txtUserNameMyPage.text = info[0]
        binding.txtUserEmailMyPage.text = info[1]
        profileImage = info[2]

        Glide.with(contentsActivity).load(profileImage).into(binding.imgProfileImage)
    }

    fun logOut() {
        var signCompleteCheck: Boolean = false
        binding.progressBarSetting.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {

            signCompleteCheck = settingViewModel.logOut(googleSignInClient)

            when (signCompleteCheck) {
                true -> {
                    Toast.makeText(
                        contentsActivity,
                        getString(R.string.do_logout),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarSetting.visibility = View.GONE
                    val intent = Intent(getActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    ActivityCompat.finishAffinity(contentsActivity)
                }
                false -> {
                    Toast.makeText(
                        contentsActivity,
                        getString(R.string.logout_exception),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarSetting.visibility = View.GONE
                }
            }
        }
    }

    fun withDraw() {
        findNavController().navigate(R.id.reCheckUserFragment)
    }

    fun toChangeProfile(){
        findNavController().navigate(R.id.changeProfileFragment)
    }

    private fun handleEvent(event: SettingViewModel.Event) = when (event) {
        is SettingViewModel.Event.Info -> getUserInfo(event.info)
        else -> {}
    }
}