package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentSettingBinding
import com.greenstory.foreststory.databinding.FragmentVerifyCommentatorBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.SettingAdapter
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel

class VerifyCommentatorFragment : Fragment() {

    val settingViewModel: SettingViewModel by activityViewModels()
    lateinit var binding: FragmentVerifyCommentatorBinding
    lateinit var contentsActivity: ContentsActivity

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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_commentator, container, false)
        binding.fragment = this@VerifyCommentatorFragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun btnBackButtonInVerify(view: View){
        activity?.onBackPressed()
    }

    fun btnVerify(view: View){

    }

}