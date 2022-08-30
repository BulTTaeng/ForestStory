package com.greenstory.foreststory.view.activity.contents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityContentsBinding
import com.greenstory.foreststory.utility.GlobalApplication
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentsActivity : AppCompatActivity() {
    lateinit var contentsController : NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var binding : ActivityContentsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contents)
        binding.activity = this@ContentsActivity

        (applicationContext as GlobalApplication).currContext = this

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_contents_container) as NavHostFragment
        contentsController = navHostFragment.navController
        binding.bottomNavi.setupWithNavController(contentsController)

    }
}