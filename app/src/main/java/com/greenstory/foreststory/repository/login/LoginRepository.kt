package com.greenstory.foreststory.repository.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.greenstory.foreststory.R
import com.greenstory.foreststory.model.userinfo.UserInfoEntity
import com.greenstory.foreststory.model.userinfo.UserInfoLogin
import com.greenstory.foreststory.utility.interfaces.login.OtherLoginInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginRepository {

    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    suspend fun emailSignUp(userInfo : UserInfoEntity , password: String) : Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(userInfo.email, password).await()
            userInfo.userId = firebaseAuth.currentUser!!.uid
            writerUserToFireStore(userInfo)
            true
        } catch (exception : Exception) {
            Log.w("[EmailSignUp]", "Email Sign Up Exception!")
            false
        }
    }

    @Throws(Exception::class)
    suspend fun writerUserToFireStore(userInfo: UserInfoEntity){
        db.collection("user").document(userInfo.userId).set(userInfo).await()
    }

    suspend fun emailLogIn(id : String , password : String) : Boolean{
        return try {
            firebaseAuth!!.signInWithEmailAndPassword(id , password).await()
            return true
        } catch (exception : Exception) {
            Log.w("[EmailSignIn]", "Email Sign In Exception!")
            return false
        }
    }

    fun getGoogleSignInClient(activity : Activity) : GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    // firebaseAuthWithGoogle
    // 0 : 원래 로그인 된 계정, 1 : 처음 구글 로그인 시도, 2 : 구글 로그인 실패
    suspend fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) : Int {
        var result = -1
        return try {

            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

            firebaseAuth.signInWithCredential(credential).await()
            val userInfoDocument = db.collection("user").document(firebaseAuth.currentUser!!.uid).get().await()

            if (userInfoDocument["name"] as String? == null) {
                result = 1
            } else {
                result = 0
            }

            result
        }catch (e:Exception){
            Log.d("googleLoginError" , e.toString())
            2
        }
    }

    //if-else를 없애고 싶은데 signUp 과 login이 한 버튼으로 작용하기 때문에 어쩔 수 없다...
    suspend fun firebaseAuthing(data : Intent?, otherLogins : OtherLoginInterface) : UserInfoEntity {

        val result = otherLogins.signUpOrLogin(data)
        val info = UserInfoEntity(
            result.name,
            result.email,
            FirebaseAuth.getInstance().currentUser!!.uid,
            false,
            result.type,
            ArrayList<String>(),
            ArrayList<String>(),
            "https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/profile.png?alt=media&token=a51a7ee7-2d15-4953-a3fc-54711bc3ddde"
        )

        return try {
            when (result.exist) {
                0 -> { //이전에 로그인 한 적 있음
                    info.explain = "exist"
                }
                1 -> { // 처음 로그인 한거임
                    writerUserToFireStore(info)
                }
            }
            info
        }catch (e:Exception){
            Log.d("googleLoginError" , e.toString())
            info.explain ="error"
            info
        }
    }


}