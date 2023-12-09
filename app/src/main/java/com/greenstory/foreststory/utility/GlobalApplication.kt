package com.greenstory.foreststory.utility

//import com.kakao.sdk.common.KakaoSdk
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {

    var currContext : Context = this
    var communityId : String = ""

    init{
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        //KakaoSdk.init(this, getString(R.string.kakao_native_key))
    }

    fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    companion object {
        lateinit var instance: GlobalApplication
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }
}