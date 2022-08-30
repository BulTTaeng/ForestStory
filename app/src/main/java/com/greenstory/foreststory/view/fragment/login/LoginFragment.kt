package com.greenstory.foreststory.view.fragment.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentLoginBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import kotlin.math.log


class LoginFragment : Fragment() {

    lateinit var loginActivity: LoginActivity
    lateinit var binding: FragmentLoginBinding

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        binding.fragment = this@LoginFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun btnToLoginEmail(view : View){
        findNavController().navigate(R.id.action_loginFragment_to_loginEmailFragment)
    }

    fun cardKakaoLogin(view: View){
        
    }

}