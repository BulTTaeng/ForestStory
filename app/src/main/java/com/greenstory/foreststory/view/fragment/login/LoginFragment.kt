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
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.viewmodel.login.LoginViewModel
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.progressBarLogin.visibility =View.VISIBLE

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                var account = task.getResult(ApiException::class.java)
                var condition : Int = -1
                var check = false
                CoroutineScope(Dispatchers.Main).launch {

                    condition = loginViewModel.firebaseAuthWithGoogle(account!!)

                    when (condition) {
                        0 -> { //이미 로그인 한 적이 있음
                            moveToContents()
                        }
                        1 -> { // 처음 로그인 한거임

                            CoroutineScope(Dispatchers.IO).launch {
                                check = loginViewModel.writerUserToFireStore(
                                    UserInfoEntity(
                                        account.displayName.toString(),
                                        account.email.toString(),
                                        FirebaseAuth.getInstance().currentUser!!.uid,
                                        false,
                                        "google",
                                        ArrayList<String>(),
                                        ArrayList<String>(),
                                    getString(R.string.basic_profile))
                                )
                            }.join()

                            if(check){
                                moveToContents()
                            }
                            else{
                                Toast.makeText(loginActivity , getString(R.string.try_later) , Toast.LENGTH_LONG).show()
                            }
                        }
                        2 -> { //error
                            Toast.makeText(loginActivity , "로그인에 실패하였습니다." , Toast.LENGTH_SHORT).show()
                            binding.progressBarLogin.visibility = View.GONE
                        }
                    }
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                binding.btnGoogleSignIn.isEnabled=true
                binding.btnGoogleSignIn.isClickable=true
                binding.progressBarLogin.visibility = View.GONE
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

    fun moveToContents() {
        binding.progressBarLogin.visibility = View.GONE
        val intent = Intent(loginActivity , ContentsActivity()::class.java)
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val GOOGLE_SIGN_IN = 9001
    }

}