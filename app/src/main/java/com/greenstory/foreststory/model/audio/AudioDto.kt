package com.greenstory.foreststory.model.audio

data class AudioDto (
    var sequence : Long,
    val link : String,
    var audioName : String,
    val commentator : String,
    var likeNum : Long,
    var isPlaying : Boolean,
    var did : String
    )