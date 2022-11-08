package com.greenstory.foreststory.model.contents

data class MountainEntity (
    val explain : String = "",
    val image : String ="",
    val name : String = "",
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val location : String =""
    )

fun MountainEntity.mapper(dis : Float): MountainDto =
    MountainDto(explain, image, name , latitude , longitude, location , distance = dis)