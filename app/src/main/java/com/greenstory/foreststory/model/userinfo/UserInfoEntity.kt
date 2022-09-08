package com.greenstory.foreststory.model.userinfo

data class UserInfoEntity(
    val name: String,
    val email : String,
    var userId : String,
    var isAdmin : Boolean,
    val type : String,
    val likeContents : ArrayList<String>,
    val likePerson : ArrayList<String>,
    var profile : String
)