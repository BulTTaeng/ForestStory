package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentSettingBinding
import com.greenstory.foreststory.databinding.FragmentVerifyCommentatorBinding
import com.greenstory.foreststory.model.setting.CommentatorVerify
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.SettingAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.VerifyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyCommentatorFragment : Fragment() {

    val commentatorViewModel : CommentatorViewModel by activityViewModels()
    lateinit var binding: FragmentVerifyCommentatorBinding
    lateinit var contentsActivity: ContentsActivity
    val verifyViewModel : VerifyViewModel by viewModels()

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

        repeatOnStarted {
            verifyViewModel.verifyEvent.collect() { event ->
                handleEvent(event)
            }
        }
    }

    fun btnBackButtonInVerify(view: View){
        activity?.onBackPressed()
    }

    fun btnVerify(view: View){

        val name = binding.edtVerifyName.text.toString()
        val birth = binding.edtVerifyBirth.text.toString()
        val serialNumber = binding.edtVerifySeqNumber.text.toString()

        binding.progressBarVerifyFragment.visibility = View.VISIBLE
        verifyViewModel.verifyForestCommentator(CommentatorVerify(name , birth , serialNumber))
    }

    fun isVerified(success : Boolean){
        if(success){
            Toast.makeText(contentsActivity , "인증 완료" , Toast.LENGTH_LONG).show()
            commentatorViewModel.userInfo?.admin = true
            contentsActivity.onBackPressed()
        }else{
            Toast.makeText(contentsActivity , getString(R.string.try_later) , Toast.LENGTH_SHORT).show()
            binding.progressBarVerifyFragment.visibility = View.GONE
        }


    }

    private fun handleEvent(event: VerifyViewModel.Event) = when (event) {
        is VerifyViewModel.Event.Success -> isVerified(event.success)
        else -> {}
    }

}