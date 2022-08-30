package com.greenstory.foreststory.view.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.greenstory.foreststory.R
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.utility.network.NetworkConnection
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.login.LoginActivity
import com.greenstory.foreststory.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val mainViewModel : MainViewModel by viewModels()
    private lateinit var connection : NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!(applicationContext as GlobalApplication).isOnline()){
            Toast.makeText(applicationContext , R.string.check_internet_connection , Toast.LENGTH_LONG).show()
            exitApp()
        }
        else{
            connection = NetworkConnection(applicationContext)
            connection.observeForever( Observer { isConnected ->
                if (!isConnected)
                {
                    Log.d("INTERNET","INTERNET CONNECTION DISCONNECTED")
                    checkInternet()
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()

        val isLogin = mainViewModel.isLogin()

        if(isLogin == 0){ // 로그인
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if (isLogin == 1){ // 콘텐츠로
            val intent = Intent(this@MainActivity, ContentsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun checkInternet(){
        if(!(applicationContext as GlobalApplication).isOnline()) {
            AlertDialog.Builder((applicationContext as GlobalApplication).currContext)
                .setTitle(getString(R.string.check_internet_connection))
                .setPositiveButton(getString(R.string.retry)) { dialogInterface: DialogInterface, i: Int ->
                    checkInternet()
                }
                .setNegativeButton(getString(R.string.close_app)) { dialogInterface: DialogInterface, i: Int ->
                    exitApp()
                }.setCancelable(false).show()
        }
    }

    fun exitApp(){
        moveTaskToBack(true)					// 태스크를 백그라운드로 이동
        finishAndRemoveTask()					// 액티비티 종료 & 태스크 리스트에서 지우기
        android.os.Process.killProcess(android.os.Process.myPid())	// 앱 프로세스 종료
    }
}