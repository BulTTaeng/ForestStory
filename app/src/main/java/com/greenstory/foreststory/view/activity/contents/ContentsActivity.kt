package com.greenstory.foreststory.view.activity.contents

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivityContentsBinding
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize

@AndroidEntryPoint
class ContentsActivity : AppCompatActivity() {
    lateinit var contentsController : NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var binding : ActivityContentsBinding
    val mountainViewModel : MountainViewModel by viewModels()
    val commentatorViewModel : CommentatorViewModel by viewModels()
    val settingViewModel : SettingViewModel by viewModels()


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

        dealDynamicLink()

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

    fun dealDynamicLink(){
        Firebase.dynamicLinks
            .getDynamicLink(intent!!)
            .addOnSuccessListener { dynamicLink ->
                var deepLink: Uri? = null
                if(dynamicLink != null){
                    deepLink = dynamicLink.link
                    val link = deepLink.toString()

                    val subString = link.split("/")

                    when(subString[2]){
                        "ted.com" -> toAudioPage(0)
                    }

                    Log.d("aaaaaaa" ,subString[2].toString())
                }

            }
            .addOnFailureListener(){
                Log.d("nnnnn" , "no message")
            }
    }

    private fun toAudioPage(case : Int){
        val intent = Intent(this , AudioPlayerActivity::class.java)
        val mountaindto = MountainDto("국립나주숲체원은 호남의 8대 명산, 생태적 가치를 지닌 금성산에 위치하여 금성산의 야생차 군락과 나주의 문화를 기반으로, 맞춤형 산림교육, 산림문화 프로그램을 제공합니다.",
            "https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/naju_forest.png?alt=media&token=faeb58c1-7c3f-4b39-8349-790acb12c74c",
        "나주숲체원",
            35.04774849999999,
            126.70006650000003,
        0.0F)

        val data = DetailLocationInfo("포이 찾아 삼만리" ,
            "https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/front.png?alt=media&token=b54603b5-c22f-4aff-856e-c7cb71fcff2a" ,
            "오늘 우리가 찾을 포이는 한국산림복지진흥원을 대표하는 귀염둥이 마스코트입니다.   " +
                    "함께 즐길 놀이는 바로 지도를 보면서 목적지를 찾아가고 안내판에 제시되어 있는 문제와 미션을 풀어 빙고를 완성해가는 미션 완성형 놀이인데요! " +
                    "숲 속 곳곳에 숨어 있는 포이를 찾아 떠날 준비 되었나요?" , "숲 해설가A" , "실내" , "2시간" , "전체")

        intent.putExtra("MOUNTAIN" , mountaindto)
        intent.putExtra("DETAIL", data)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}