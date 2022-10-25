package com.greenstory.foreststory.model.userinfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfoEntity(
    val name: String = "",
    val email : String = "",
    var userId : String ="",
    var admin : Boolean =false,
    val type : String = "",
    val likeContents : ArrayList<String> = ArrayList<String>(),
    val likePerson : ArrayList<String> = ArrayList<String>(),
    var profile : String =""
): Parcelable