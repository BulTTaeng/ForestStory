package com.greenstory.foreststory.model.contents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentatorDto (
    val audio : ArrayList<String> = ArrayList<String>(),
    val likedNum : Long = 0L,
    val mountain : ArrayList<String> = ArrayList<String>(),
    val id : String = "",
    val name : String = "",
    val profile : String = "",
    val explain : String ="",
    val hashTag : String =""
        ): Parcelable