package com.greenstory.foreststory.view.adapter

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.Nullable
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.greenstory.foreststory.model.audio.AudioEntity
import com.greenstory.foreststory.view.activity.contents.ContentsActivity


class DescriptionAdapter(val context : Context , val bitmap: Bitmap? , val list : MutableList<AudioEntity>) : MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): String {
        val window = player.currentWindowIndex
        return list[player.currentMediaItemIndex].audioName
    }

    @Nullable
    override fun getCurrentContentText(player: Player): String? {
        val window = player.currentWindowIndex
        return list[player.currentMediaItemIndex].commentator
    }

    @Nullable
    override fun getCurrentLargeIcon(
        player: Player,
        callback: BitmapCallback
    ): Bitmap? {
        return bitmap
    }

    @Nullable
    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val window = player.currentWindowIndex
        val intent = Intent(context , ContentsActivity::class.java)
        val packageIntent = context.packageManager.getLaunchIntentForPackage("com.greenstory.foreststory")

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            packageIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }
}