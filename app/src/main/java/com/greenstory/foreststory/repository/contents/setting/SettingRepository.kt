package com.greenstory.foreststory.repository.contents.setting

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingRepository {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    fun logOut(){
        firebaseAuth.signOut()
    }
}