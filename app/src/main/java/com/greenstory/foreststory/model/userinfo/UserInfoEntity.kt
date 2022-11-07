package com.greenstory.foreststory.model.userinfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfoEntity(
    var name: String = "",
    var email : String = "",
    var userId : String ="",
    var admin : Boolean =false,
    var type : String = "",
    val likeContents : ArrayList<String> = ArrayList<String>(),
    val likePerson : ArrayList<String> = ArrayList<String>(),
    var profile : String ="",
    var nickName : String ="",
    var explain : String = ""
): Parcelable