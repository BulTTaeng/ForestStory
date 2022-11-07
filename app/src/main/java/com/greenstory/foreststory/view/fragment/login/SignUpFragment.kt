package com.greenstory.foreststory.view.fragment.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentSignUpBinding
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.viewmodel.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding
    lateinit var loginActivity: LoginActivity
    val loginViewModel: LoginViewModel by activityViewModels()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.fragment = this@SignUpFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarSignUp.visibility = View.GONE

        setTextListener()
    }

    fun btnToMakeProfile(view: View) {
        if (binding.edtSignupPassword.text == null || binding.edtSignupPassword.text.toString() == "") {
            Toast.makeText(loginActivity, "비밀번호를 입력해 주세요", Toast.LENGTH_LONG).show()
        } else if (binding.edtSignupPassword2.text == null || binding.edtSignupPassword2.text.toString() == "") {
            Toast.makeText(loginActivity, "비밀번호를 확인해 주세요", Toast.LENGTH_LONG).show()
        } else if (binding.edtSignupEmail.text == null || binding.edtSignupEmail.text.toString() == "") {
            Toast.makeText(loginActivity, "아이디를 입력해 주세요", Toast.LENGTH_LONG).show()
        } else if (binding.edtSignupPassword.text!!.length < 6) {
            Toast.makeText(loginActivity, getText(R.string.password_length), Toast.LENGTH_LONG)
                .show()
        } else if (binding.edtSignupPassword.text.toString() != binding.edtSignupPassword2.text.toString()) {
            Toast.makeText(loginActivity, getText(R.string.password_wrong), Toast.LENGTH_LONG)
                .show()
        }
        else{
            doubleCheckEmail()
        }

    }

    fun doubleCheckEmail() {

        AlertDialog.Builder(loginActivity)
            .setTitle(R.string.double_check_email)
            .setMessage(R.string.double_check_email_message)
            .setPositiveButton("아니요") { _: DialogInterface, i: Int ->
            }.setNegativeButton("네") { _: DialogInterface, i: Int ->
                signUpWithEmail()
            }.setCancelable(false).show()

    }

    fun signUpWithEmail() {

        binding.progressBarSignUp.visibility = View.VISIBLE
        loginViewModel.userInfo.email = binding.edtSignupEmail.text.toString()
        loginViewModel.userInfo.admin = false
        loginViewModel.userInfo.profile = getString(R.string.basic_profile)
        loginViewModel.userInfo.type = "email"
        loginViewModel.password = binding.edtSignupPassword.text.toString()

        CoroutineScope(Dispatchers.Main).launch {
            val success = loginViewModel.emailSignUp()
            if(success){
                findNavController().navigate(R.id.makeProfileFragment)
            }
            else{
                Toast.makeText(loginActivity, getText(R.string.try_later), Toast.LENGTH_SHORT).show()
            }

            binding.progressBarSignUp.visibility = View.GONE
        }



//            val success =
//                loginViewModel.emailSignUp(userInfo, binding.edtSignupPassword.text.toString())
//
//            if (success) {
//                val intent = Intent(loginActivity, ContentsActivity::class.java)
//                startActivity(intent)
//                loginActivity.finish()
//            } else {
//                Toast.makeText(loginActivity, getText(R.string.try_later), Toast.LENGTH_LONG).show()
//            }

    }

    private fun setTextListener(){
        binding.edtSignupEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                if(!s.toString().contains("@")){
                    binding.tilSignupEmail.error = "올바른 이메일 주소가 아닙니다"
                }
                else{
                    binding.tilSignupEmail.error = ""
                }
            }
        })

        binding.edtSignupPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                if(s.length < 6){
                    binding.tilSignupPassword.error = "비밀번호는 최소 6자리 입니다"
                }
                else{
                    binding.tilSignupPassword.error = ""
                }
            }
        })

        binding.edtSignupPassword2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                if(s.toString() != binding.edtSignupPassword.text.toString()){
                    binding.tilSignupPassword2.error = "비밀번호가 다릅니다"
                }
                else{
                    binding.tilSignupPassword2.error =""
                }
            }
        })
    }

}