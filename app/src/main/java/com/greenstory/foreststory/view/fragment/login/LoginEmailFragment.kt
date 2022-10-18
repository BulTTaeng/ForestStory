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
import androidx.navigation.fragment.findNavController
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentLoginEmailBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.viewmodel.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginEmailFragment : Fragment() {

    lateinit var binding : FragmentLoginEmailBinding
    val loginViewModel : LoginViewModel by activityViewModels()
    lateinit var loginActivity: LoginActivity

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login_email, container, false)
        binding.fragment = this@LoginEmailFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarLoginEmail.visibility = View.GONE
    }

    fun btnLogIn(view: View){

        if(binding.edtEmail.text.isEmpty()){
            Toast.makeText(loginActivity , getText(R.string.email) , Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.edtPassword.text.isEmpty()){
            Toast.makeText(loginActivity , getText(R.string.email) , Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBarLoginEmail.visibility = View.VISIBLE
        binding.btnLogIn.isEnabled=false

        CoroutineScope(Dispatchers.Main).launch {
            val success = loginViewModel.emailLogIn(binding.edtEmail.text.toString() , binding.edtPassword.text.toString())

            if(success.await()){
                val intent = Intent(loginActivity , ContentsActivity::class.java)
                startActivity(intent)
                loginActivity.finish()
            }
            else{
                Toast.makeText(loginActivity , getText(R.string.check_id_password) , Toast.LENGTH_SHORT).show()
                binding.progressBarLoginEmail.visibility = View.GONE
                binding.btnLogIn.isEnabled=true
            }
        }

    }

    fun btnToSignUp(view: View){
        findNavController().navigate(R.id.action_loginEmailFragment_to_signUpFragment)
    }

    fun btnFindPassword(view: View){

    }

}