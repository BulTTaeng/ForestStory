package com.greenstory.foreststory.view.activity.contents

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityContentsBinding
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentsActivity : AppCompatActivity() {
    lateinit var contentsController : NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var binding : ActivityContentsBinding
    val mountainViewModel : MountainViewModel by viewModels()
    val commentatorViewModel : CommentatorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contents)
        binding.activity = this@ContentsActivity

        (applicationContext as GlobalApplication).currContext = this

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_contents_container) as NavHostFragment
        contentsController = navHostFragment.navController
        binding.bottomNavi.setupWithNavController(contentsController)

        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        )

        requirePermissions(permissions, 999)



    }

    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //permissionGranted(requestCode)
        } else {
            val isAllPermissionsGranted =
                permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
            if (isAllPermissionsGranted) {
                //permissionGranted(requestCode)
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            //permissionGranted(requestCode)
        } else {
            Toast.makeText(this, "앱 접근 권한이 거부 되었습니다. 추후 설정->앱 에서 권한 설정을 해주세요", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}