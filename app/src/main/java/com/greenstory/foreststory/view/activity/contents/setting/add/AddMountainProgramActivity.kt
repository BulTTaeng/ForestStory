package com.greenstory.foreststory.view.activity.contents.setting.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.greenstory.foreststory.R
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.add.AddProgramViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddMountainProgramActivity : AppCompatActivity() {

    lateinit var addMountainController : NavController
    lateinit var navHostFragment: NavHostFragment
    val mountainViewModel : MountainViewModel by viewModels()
    val addProgramViewModel : AddProgramViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mountain_program)
        (applicationContext as GlobalApplication).currContext = this

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_add_mountain_container) as NavHostFragment
        addMountainController = navHostFragment.navController
    }

    fun btnBackButtonInAddMountain(view : View){
        onBackPressed()
    }
}