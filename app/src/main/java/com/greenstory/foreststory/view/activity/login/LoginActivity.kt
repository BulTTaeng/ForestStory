package com.greenstory.foreststory.view.activity.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.greenstory.foreststory.R
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var loginController : NavController
    lateinit var navHostFragment: NavHostFragment

    val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (applicationContext as GlobalApplication).currContext = this

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_login_container) as NavHostFragment
        loginController = navHostFragment.navController
    }
}