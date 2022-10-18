package com.greenstory.foreststory.model.audio

import com.google.android.exoplayer2.MediaItem

data class Audios (
        var audioList : ArrayList<AudioEntity> = ArrayList<AudioEntity>(),
        var audioLink : ArrayList<MediaItem> = ArrayList<MediaItem>()
)