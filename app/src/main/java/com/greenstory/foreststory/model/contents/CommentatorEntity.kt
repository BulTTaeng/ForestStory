package com.greenstory.foreststory.model.contents

data class CommentatorEntity (
    val likedNum : Long = 0L,
    val mountain : MutableMap<String, ArrayList<String>> = mutableMapOf<String, ArrayList<String>>(),
    val id : String = "",
    val name : String = "",
    val profile : String = "",
    val explain : String ="",
    val hashTag : String ="",
    val mountains : ArrayList<String> = ArrayList<String>(),
    val nickName : String =""
    )

fun CommentatorEntity.mapper( ): CommentatorDto =
    CommentatorDto(likedNum, mountain, id, name, profile, explain, hashTag , mountains , nickName)