package com.greenstory.foreststory.view.fragment.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
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

    lateinit var binding : FragmentSignUpBinding
    lateinit var loginActivity: LoginActivity
    val loginViewModel : LoginViewModel by activityViewModels()

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up, container, false)
        binding.fragment = this@SignUpFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun btnSignUp(view: View){

        if(binding.edtSignupPassword.text.length < 6){
            Toast.makeText(loginActivity , getText(R.string.password_length) , Toast.LENGTH_LONG).show()
        }
        else if(binding.edtSignupPassword.text.toString() != binding.edtSignupPassword2.text.toString()){
            Toast.makeText(loginActivity , getText(R.string.password_wrong) , Toast.LENGTH_LONG).show()
        }
        else if(binding.edtSignupEmail.text.isEmpty() || binding.edtSignupName.text.isEmpty()){
            Toast.makeText(loginActivity , getText(R.string.id_name_empty) , Toast.LENGTH_LONG).show()
        }
        else{
            CoroutineScope(Dispatchers.Main).launch {
                val userInfo = UserInfoEntity(binding.edtSignupName.text.toString() ,
                    binding.edtSignupEmail.text.toString(),
                    "",
                    false,
                    "email" ,
                    ArrayList<String>() ,
                    ArrayList<String>(),
                    getString(R.string.basic_profile))

                val success = loginViewModel.emailSignUp(userInfo ,binding.edtSignupPassword.toString())

                if(success){
                    val intent = Intent(loginActivity , ContentsActivity::class.java)
                    startActivity(intent)
                    loginActivity.finish()
                }
                else{
                    Toast.makeText(loginActivity , getText(R.string.try_later) , Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}