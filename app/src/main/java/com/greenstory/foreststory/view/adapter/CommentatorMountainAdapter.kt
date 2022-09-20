package com.greenstory.foreststory.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenstory.foreststory.databinding.ItemMountainBinding
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity
import com.greenstory.foreststory.view.adapter.MountainAdapter.Companion.MOUNTAIN_DIFF_CALLBACK

class CommentatorMountainAdapter : ListAdapter<MountainDto, CommentatorMountainAdapter.CommentatorMountainViewHolder>(MOUNTAIN_DIFF_CALLBACK){

    lateinit var binding : ItemMountainBinding

    inner class CommentatorMountainViewHolder(
        private val binding: ItemMountainBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: MountainDto) = with(binding) {
            binding.mountainDto = data

            Glide.with(itemView.context).load(data.image).into(binding.imgMountainImage)

            itemView.setOnClickListener {
                val intent = Intent(it.context , AudioPlayerActivity::class.java)
                intent.putExtra("MOUNTAINNAME" , data.name)
                intent.putExtra("COMMENTATORINFO" , (itemView.context as CommentatorActivity).commentatorDto )
                it.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentatorMountainViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemMountainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CommentatorMountainViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: CommentatorMountainViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

}