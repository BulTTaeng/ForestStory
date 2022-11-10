package com.greenstory.foreststory.view.activity.contents.setting.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityAddMountainProgramBinding
import com.greenstory.foreststory.databinding.ActivityContentsBinding
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.view.fragment.contents.setting.add.AddMountainProgramFragmentDirections
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.add.AddProgramViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddMountainProgramActivity : AppCompatActivity() {

    lateinit var addMountainController : NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var binding : ActivityAddMountainProgramBinding
    val mountainViewModel : MountainViewModel by viewModels()
    val addProgramViewModel : AddProgramViewModel by viewModels()
    val settingViewModel : SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_mountain_program)
        binding.activity = this@AddMountainProgramActivity
        (applicationContext as GlobalApplication).currContext = this

        val haveName  = intent.getStringExtra("MOUNTAINNAME")
        var detailLocationInfo = intent.getParcelableExtra<DetailLocationInfo>("DETAILINFO")

        if(detailLocationInfo == null){
            detailLocationInfo = DetailLocationInfo()
        }

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_add_mountain_container) as NavHostFragment
        addMountainController = navHostFragment.navController

        if(haveName != null){
            addMountainController.navigate(AddMountainProgramFragmentDirections.actionAddMountainFragmentToAddProgramFragment(haveName , detailLocationInfo))
        }
    }

    fun btnBackButtonInAddMountain(view : View){
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}