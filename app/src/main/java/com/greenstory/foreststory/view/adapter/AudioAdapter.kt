package com.greenstory.foreststory.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.greenstory.foreststory.databinding.ItemAudioBinding
import com.greenstory.foreststory.model.audio.AudioDto
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.fragment.audio.AudioPlayerFragment

class AudioAdapter(val player : ExoPlayer , val fragmentContext : AudioPlayerFragment) : ListAdapter<AudioDto, AudioAdapter.AudioViewHolder>(AUDIO_DIFF_CALLBACK){

    lateinit var binding : ItemAudioBinding
    var currLoc = -1L

    inner class AudioViewHolder(
        private val binding: ItemAudioBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: AudioDto) = with(binding) {
            binding.audioDto = data
            binding.itemArtistTextView.text = data.commentator
            binding.itemTrackTextView.text = data.audioName

            Glide.with(itemView.context).load("https://firebasestorage.googleapis.com/v0/b/foreststory-390cf.appspot.com/o/end.png?alt=media&token=c5b00f7d-04cb-455f-94ea-4209b7617c08").into(binding.imgCoverImage)


            itemView.setOnClickListener {

                if(currLoc.toInt() != absoluteAdapterPosition) {

                    if(currLoc != -1L) {
                        currentList[currLoc.toInt()].isPlaying = false
                        notifyItemChanged(currLoc.toInt())
                    }

                    currentList[absoluteAdapterPosition].isPlaying = true
                    notifyItemChanged(absoluteAdapterPosition)

                    player.seekTo(absoluteAdapterPosition , 0)
                    player.play()

                    currLoc = absoluteAdapterPosition.toLong()
                }
                fragmentContext.updateTextInPlayer(data.audioName)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemAudioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AudioViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    companion object {
        val AUDIO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<AudioDto>() {
            override fun areItemsTheSame(oldItem: AudioDto, newItem: AudioDto): Boolean =
                oldItem.sequence == newItem.sequence

            override fun areContentsTheSame(oldItem: AudioDto, newItem: AudioDto): Boolean =
                oldItem == newItem
        }
    }

}