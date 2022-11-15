package com.greenstory.foreststory.view.activity.contents.setting.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityAddAudioBinding
import com.greenstory.foreststory.databinding.ActivityAddMountainProgramBinding
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.viewmodel.contents.setting.add.AddAudioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAudioActivity : AppCompatActivity() {

    lateinit var addAudioController : NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var binding : ActivityAddAudioBinding
    val addAudioViewModel : AddAudioViewModel by viewModels()
    var size = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_audio)
        binding.activity = this@AddAudioActivity
        (applicationContext as GlobalApplication).currContext = this

        addAudioViewModel.mountainName = intent.getStringExtra("MOUNTAINNAME").toString()
        val temp = intent.getParcelableExtra<DetailLocationInfo>("DETAILINFO")
        size = intent.getLongExtra("SIZE" , 0L)
        temp?.let {
            addAudioViewModel.detailInfo = temp
        }

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_add_audio_container) as NavHostFragment
        addAudioController = navHostFragment.navController
    }

    fun btnBackButtonInAddAudio(view : View){
        super.onBackPressed()
    }
}