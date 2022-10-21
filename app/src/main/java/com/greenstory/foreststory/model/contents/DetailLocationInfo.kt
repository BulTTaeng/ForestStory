package com.greenstory.foreststory.model.contents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailLocationInfo (
    val name : String ="",
    val image : String="",
    val explain : String="",
    val commentator : String="",
    val location : String="",
    val time : String="",
    val attendance : String=""
    ): Parcelable