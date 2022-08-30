package com.greenstory.foreststory.model

data class UserInfoEntity(
    val name: String,
    val email : String,
    val password : String,
    var userId : String
)