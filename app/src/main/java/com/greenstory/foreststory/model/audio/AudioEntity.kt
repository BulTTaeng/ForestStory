package com.greenstory.foreststory.model.audio

import java.io.Serializable


data class AudioEntity (
    val audioName : String ="",
    val commentator : String ="",
    var likeNum : Long = 0L,
    var likeId : ArrayList<String> = ArrayList<String>(),
    val link : String = ""
) : Serializable
