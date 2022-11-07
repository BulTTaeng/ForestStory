package com.greenstory.foreststory.view.fragment.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentLoginBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.utility.classes.login.GoogleLogin
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.viewmodel.login.LoginViewModel
import com.rommansabbir.animationx.Fade
import com.rommansabbir.animationx.Slide
import com.rommansabbir.animationx.animationXFade
import com.rommansabbir.animationx.animationXSlide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log


class LoginFragment : Fragment() {

    lateinit var loginActivity: LoginActivity
    lateinit var binding: FragmentLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        binding.fragment = this@LoginFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarLogin.visibility = View.GONE

        googleSignInClient = loginViewModel.getGoogleSignInClient(loginActivity)

        binding.btnGoogleSignIn.setOnClickListener{
            signInGoogle()
        }
        binding.imgNuruLogoLogin.animationXSlide(Slide.SLIDE_IN_DOWN , duration = 1200)

        binding.btnGoogleSignIn.animationXSlide(Slide.SLIDE_IN_UP , duration = 500)
        binding.cardKakaoLogin.animationXSlide(Slide.SLIDE_IN_UP, duration = 500 )
        binding.consToEmailLoginPage.animationXSlide(Slide.SLIDE_IN_UP , duration = 500)

//        binding.btnGoogleSignIn.animationXFade(Fade.FADE_IN_UP)
//        binding.cardKakaoLogin.animationXFade(Fade.FADE_IN_UP)
//        binding.consClickHere.animationXFade(Fade.FADE_IN_UP)
//        binding.consToEmailLoginPage.animationXFade(Fade.FADE_IN_UP)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.progressBarLogin.visibility =View.VISIBLE

        if(requestCode == GOOGLE_SIGN_IN){
            val otherLogin = GoogleLogin()
            CoroutineScope(Dispatchers.Main).launch {

                when (loginViewModel.firebaseAuthing(data, otherLogin)) {
                    0 -> { //전에 로그인 한적 있음
                        moveToContents()
                    }
                    1 -> { // 처음임
                        moveToMakeProfileFragment()
                    }
                    else -> {
                        Toast.makeText(loginActivity , "로그인에 실패하였습니다." , Toast.LENGTH_SHORT).show()
                        binding.progressBarLogin.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun btnToLoginEmail(view : View){
        findNavController().navigate(R.id.action_loginFragment_to_loginEmailFragment)
    }

    fun cardKakaoLogin(view: View){
        
    }

    private fun signInGoogle() {
        binding.btnGoogleSignIn.isEnabled=false
        binding.btnGoogleSignIn.isClickable=false
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        //activity?.finish()
    }

    private fun moveToContents() {
        binding.progressBarLogin.visibility = View.GONE
        val intent = Intent(loginActivity , ContentsActivity()::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun moveToMakeProfileFragment(){
        binding.progressBarLogin.visibility = View.GONE
        findNavController().navigate(R.id.makeProfileFragment)
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val GOOGLE_SIGN_IN = 9001
    }

}