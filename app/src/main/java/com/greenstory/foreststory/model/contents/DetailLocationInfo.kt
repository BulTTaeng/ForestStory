package com.greenstory.foreststory.model.contents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailLocationInfo (
    var name : String ="",
    var image : String="",
    var explain : String="",
    val commentator : String="",
    var location : String="",
    var time : String="",
    var attendance : String=""
    ): Parcelable