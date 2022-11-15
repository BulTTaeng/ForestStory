package com.greenstory.foreststory.model.audio

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class AudioEntity (
    var sequence : Long = 0L,
    var audioName : String ="",
    var commentator : String ="",
    var likeNum : Long = 0L,
    var likeId : ArrayList<String> = ArrayList<String>(),
    var link : String = "",
    var did : String = ""
) : Serializable , Parcelable

fun AudioEntity.mapper(): AudioDto =
    AudioDto(sequence , link , audioName , commentator , likeNum , false , did)