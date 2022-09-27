package com.greenstory.foreststory.repository.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.utility.GlobalApplication
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WithdrawalService() : Service() {
    private var startMode: Int = 0             // 서비스가 종료되면 어떻게 할지 명시
    private var binder: IBinder? = null        // bind한 클라이언트를 위한 인터페이스
    private var allowRebind: Boolean = false   // onRebind가 allow 되는지 명시
    lateinit var broadcaster :  LocalBroadcastManager



    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        // 이 서비스가 맨 처음으로 call 됐을 때만 수행

        broadcaster = LocalBroadcastManager.getInstance(GlobalApplication.ApplicationContext());
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        revokeAccess()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun sendMessage(message: String) {
        val intent = Intent("DONE")
        intent.putExtra("KEYVALUE", message)
        broadcaster.sendBroadcast(intent)
    }

    fun revokeAccess() {
        var check : Boolean = false
        CoroutineScope(Dispatchers.Main).launch {

            check = deleteAccount()

            when (check) {
                true -> sendMessage("good")
                false -> sendMessage("error")
            }
        }
    }

    suspend fun deleteAccount() : Boolean {
        val fAuth: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val db = FirebaseFirestore.getInstance()
        val uuid = fAuth.uid
        var check : Boolean = false
        var isSuccess : Boolean = false
        var loginType : String = ""

        return try {

            val userDocument = db.collection("user").document(uuid).get().await()

            loginType = userDocument["type"].toString()

            fAuth.delete().await()

            FirebaseAuth.getInstance().signOut()

            db.collection("user").document(uuid).delete().await()

            if (loginType.equals("kakao")) {
                Log.d("카카오로그인", "카카오 회원탈퇴")
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        Log.d("카카오로그인", "회원 탈퇴 실패")
                    } else {
                        Log.d("카카오로그인", "회원 탈퇴 성공")
                    }
                }
            }

            true
        }catch (e : Exception){
            Log.d("deleteAccount" , e.toString())
            false
        }
    }
}