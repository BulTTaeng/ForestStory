package com.greenstory.foreststory.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.greenstory.foreststory.databinding.ItemAudioBinding
import com.greenstory.foreststory.model.audio.AudioDto
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.fragment.audio.AudioPlayerFragment

class AudioAdapter(val player : ExoPlayer) : ListAdapter<AudioDto, AudioAdapter.AudioViewHolder>(AUDIO_DIFF_CALLBACK){

    lateinit var binding : ItemAudioBinding
    var currLoc = -1L

    inner class AudioViewHolder(
        private val binding: ItemAudioBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: AudioDto) = with(binding) {
            binding.audioDto = data
            binding.itemArtistTextView.text = data.commentator
            binding.itemTrackTextView.text = data.audioName


            itemView.setOnClickListener {
                if(currLoc.toInt() != absoluteAdapterPosition) {
                    player.seekTo(absoluteAdapterPosition , 0)
                    player.play()
                    binding.setChecked()
                    notifyItemChanged(currLoc.toInt())
                    currLoc = absoluteAdapterPosition.toLong()
                }
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

    private fun ItemAudioBinding.setChecked() = consItemAudio.setBackgroundColor(Color.GRAY)

    companion object {
        val AUDIO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<AudioDto>() {
            override fun areItemsTheSame(oldItem: AudioDto, newItem: AudioDto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AudioDto, newItem: AudioDto): Boolean =
                oldItem == newItem
        }
    }

}