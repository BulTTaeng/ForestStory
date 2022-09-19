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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentSettingBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.view.adapter.SettingAdapter
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import kotlinx.coroutines.*

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
    lateinit var settingViewModel: SettingViewModel
    lateinit var contentsActivity: ContentsActivity
    lateinit var adapter: SettingAdapter
    private lateinit var googleSignInClient: GoogleSignInClient

    var isSetting = false
    private lateinit var callback: OnBackPressedCallback
    private lateinit var callback2: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentsActivity = context as ContentsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isSetting) {
                    adapter.setOnClickListener(
                        listOf(
                            "자격증 인증",
                            "관심 목록",
                            "프로필 변경",
                            "내 오디오 보기"
                        )
                    ) {
                        when (it) {
                            "자격증 인증" -> Log.d("111", "111")
                            "관심 목록" -> Log.d("222", "222")
                            "프로필 변경" -> Log.d("333", "333")
                            "내 오디오 보기" -> Log.d("444", "$444")
                        }
                    }
                    isSetting = false
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

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
        settingViewModel = ViewModelProvider(contentsActivity).get(SettingViewModel::class.java)
        googleSignInClient = settingViewModel.getGoogleSignInClient(contentsActivity)
        getUserInfo()
        observeMyInfo()
        initRecyclerView()
    }

    fun btnSetting(view: View) {
        reSettingRecyclerView()
    }

    fun initRecyclerView() {
        adapter = SettingAdapter()
        adapter.setOnClickListener(
            listOf(
                "자격증 인증",
                "관심 목록",
                "프로필 변경",
                "내 오디오 보기"
            )
        ) {
            when (it) {
                "자격증 인증" -> Log.d("111", "111")
                "관심 목록" -> Log.d("222", "222")
                "프로필 변경" -> Log.d("333", "333")
                "내 오디오 보기" -> Log.d("444", "$444")
            }
        }
        binding.recyclerSetting.layoutManager = LinearLayoutManager(contentsActivity)
        binding.recyclerSetting.adapter = adapter
    }

    fun reSettingRecyclerView() {
        isSetting = true
        adapter.setOnClickListener(
            listOf(
                "로그아웃",
                "회원 탈퇴"
            )
        ) {
            when (it) {
                "로그아웃" -> logOut()
                "회원 탈퇴" -> withDraw()
            }
        }
    }

    fun getUserInfo() {
        settingViewModel.getUserNameAndEmailProfileImage()
    }

    fun observeMyInfo() {
        settingViewModel.myInfo?.observe(viewLifecycleOwner, Observer {

            var profileImage = ""
            binding.txtUserNameMyPage.text = it[0]
            binding.txtUserEmailMyPage.text = it[1]
            profileImage = it[2]

            Glide.with(contentsActivity).load(profileImage).into(binding.imgProfileImage)

        })
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
}