package com.greenstory.foreststory.model.audio

data class AudioDto (
    var id : Long,
    val link : String,
    val audioName : String,
    val commentator : String,
    var likeNum : Long,
    var isPlaying : Boolean
    )