package com.greenstory.foreststory.view.activity.contents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.greenstory.foreststory.R
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentatorActivity : AppCompatActivity() {

    lateinit var commentatorDto: CommentatorDto
    lateinit var commentatorController : NavController
    lateinit var navHostFragment: NavHostFragment
    val mountainViewModel: MountainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commentator)
        (applicationContext as GlobalApplication).currContext = this

        val temp = intent.getParcelableExtra<CommentatorDto>("INFO")

        if(temp == null){
            Toast.makeText(this , getString(R.string.try_later) , Toast.LENGTH_SHORT).show()
            this.finish()
        }
        else{
            commentatorDto = temp
        }

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_commentator_container) as NavHostFragment
        commentatorController = navHostFragment.navController
    }
}