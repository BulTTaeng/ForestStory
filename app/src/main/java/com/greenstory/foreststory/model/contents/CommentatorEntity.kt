package com.greenstory.foreststory.model.contents

data class CommentatorEntity (
    val likedNum : Long = 0L,
    val mountain : MutableMap<String, ArrayList<String>> = mutableMapOf("" to ArrayList<String>()),
    val id : String = "",
    val name : String = "",
    val profile : String = "",
    val explain : String ="",
    val hashTag : String ="",
    val mountains : ArrayList<String> = ArrayList<String>(),
    val nickName : String =""
    )