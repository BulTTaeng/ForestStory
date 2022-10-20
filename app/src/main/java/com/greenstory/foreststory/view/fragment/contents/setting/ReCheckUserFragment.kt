package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentReCheckUserBinding
import com.greenstory.foreststory.repository.service.WithdrawalService
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ReCheckUserFragment : Fragment() {

    lateinit var binding : FragmentReCheckUserBinding
    lateinit var contentsActivity : ContentsActivity
    val settingViewModel: SettingViewModel by activityViewModels()

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_re_check_user, container, false)
        binding.fragment = this@ReCheckUserFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarReCheckUser.visibility = View.GONE


    }

    fun startService()
    {
        val intent = Intent(contentsActivity, WithdrawalService::class.java)
        contentsActivity.startService(intent)
        settingViewModel.registerReceiver()
    }

    fun endService()
    {
        val intent = Intent(contentsActivity, WithdrawalService::class.java)
        contentsActivity.stopService(intent)
        settingViewModel.unRegister()
    }

    fun observeReceiver(){

        settingViewModel.fetchMyReceiver().observe(
            viewLifecycleOwner, Observer {
                when(it){
                    "good" -> goodWithDraw()
                    else -> errorWithDraw()
                }
            }
        )
    }

    fun goodWithDraw(){
        Toast.makeText(contentsActivity, "회원 탈퇴에 성공했습니다", Toast.LENGTH_SHORT).show()
        endService()
        val intent = Intent(contentsActivity, LoginActivity::class.java)
        startActivity(intent)
        ActivityCompat.finishAffinity(contentsActivity)
    }

    fun errorWithDraw(){
        binding.progressBarReCheckUser.visibility = View.GONE
        Toast.makeText(contentsActivity, getString(R.string.try_later), Toast.LENGTH_SHORT).show()
        endService()
    }

    fun btnBackButtonInReCheckUser(view:View){
        findNavController().navigate(R.id.settingFragment)
    }

    fun btnRecheckUser(view: View){
        if(binding.edtReCheckEmail.text.isEmpty()){
            Toast.makeText(contentsActivity , getString(R.string.email) , Toast.LENGTH_SHORT).show()
        }
        else if(binding.edtReCheckPassword.text.isEmpty()){
            Toast.makeText(contentsActivity , getString(R.string.password) , Toast.LENGTH_SHORT).show()
        }
        else if(binding.edtReCheckPassword.text.toString().length < 6){
            Toast.makeText(contentsActivity , getString(R.string.password_length) , Toast.LENGTH_SHORT).show()
        }
        else{
            binding.progressBarReCheckUser.visibility = View.VISIBLE
            val acct = GoogleSignIn.getLastSignedInAccount(contentsActivity)
            CoroutineScope(Dispatchers.Main).launch {
                if(settingViewModel.reCheckUser(binding.edtReCheckEmail.text.toString() , binding.edtReCheckPassword.text.toString() , acct)){
                    startService()
                    observeReceiver()
                }
                else{
                    if(settingViewModel.reCheckUser(binding.edtReCheckEmail.text.toString() , "nuru1234" , acct)){
                        startService()
                        observeReceiver()
                    }
                    else{
                        Log.d("wwwwwwwwwwwwwwww" , binding.edtReCheckEmail.text.toString() + binding.edtReCheckPassword.text.toString())
                        Toast.makeText(contentsActivity , "비밀번호와 아이디를 확인해 주세요", Toast.LENGTH_LONG).show()
                        binding.progressBarReCheckUser.visibility = View.GONE
                    }
                }
            }
        }

    }


}