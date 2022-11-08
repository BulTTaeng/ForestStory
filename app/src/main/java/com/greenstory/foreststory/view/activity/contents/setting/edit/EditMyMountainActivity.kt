package com.greenstory.foreststory.view.activity.contents.setting.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityContentsBinding
import com.greenstory.foreststory.databinding.ActivityEditMyMountainBinding
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMyMountainActivity : AppCompatActivity() {
    lateinit var editMyMountainController : NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var binding : ActivityEditMyMountainBinding
    var name : String = ""

    val mountainViewModel : MountainViewModel by viewModels()
    val commentatorViewModel : CommentatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_my_mountain)
        binding.activity = this@EditMyMountainActivity

        (applicationContext as GlobalApplication).currContext = this

        name = intent.getStringExtra("MOUNTAINNAME").toString()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_edit_my_mountain_container) as NavHostFragment
        editMyMountainController = navHostFragment.navController

    }
}