package com.greenstory.foreststory.model.audio

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class AudioEntity (
    var audioName : String ="",
    var commentator : String ="",
    var likeNum : Long = 0L,
    var likeId : ArrayList<String> = ArrayList<String>(),
    var link : String = ""
) : Serializable , Parcelable

fun AudioEntity.mapper(index : Long): AudioDto =
    AudioDto(id = index , link , audioName , commentator , likeNum , false)