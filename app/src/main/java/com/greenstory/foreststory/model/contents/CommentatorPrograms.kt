package com.greenstory.foreststory.model.contents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentatorPrograms (
    val detailProgramLists : ArrayList<String> = ArrayList<String>()
): Parcelable