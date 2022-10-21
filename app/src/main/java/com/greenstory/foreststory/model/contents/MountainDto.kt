package com.greenstory.foreststory.model.contents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MountainDto (
    val explain : String = "",
    val image : String ="",
    val name : String = "",
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    var distance : Float = 0.0F
    ) : Parcelable