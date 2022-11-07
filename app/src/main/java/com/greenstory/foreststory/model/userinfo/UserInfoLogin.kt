package com.greenstory.foreststory.model.userinfo

data class UserInfoLogin (
    var name : String ="",
    var email : String = "",
    var exist : Int = 2,
    var type : String =""
    )