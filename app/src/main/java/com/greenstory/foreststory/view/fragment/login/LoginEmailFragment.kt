package com.greenstory.foreststory.view.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentLoginEmailBinding

class LoginEmailFragment : Fragment() {

    lateinit var binding : FragmentLoginEmailBinding

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

    fun btnLogIn(view: View){

    }

    fun btnToSignUp(view: View){
        findNavController().navigate(R.id.action_loginEmailFragment_to_signUpFragment)
    }

    fun btnFindPassword(view: View){

    }

}