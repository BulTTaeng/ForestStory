package com.greenstory.foreststory.model.audio

import java.io.Serializable


data class AudioEntity (
    val audioName : String ="",
    val commentator : String ="",
    var likeNum : Long = 0L,
    val link : String = ""
) : Serializable
