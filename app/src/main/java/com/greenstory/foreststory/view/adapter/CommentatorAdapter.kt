package com.greenstory.foreststory.view.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenstory.foreststory.databinding.ItemCommentatorBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity


class CommentatorAdapter : ListAdapter<CommentatorDto, CommentatorAdapter.CommentatorViewHolder>(COMMENTATOR_DIFF_CALLBACK){

    lateinit var binding : ItemCommentatorBinding

    inner class CommentatorViewHolder(
        private val binding: ItemCommentatorBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: CommentatorDto) = with(binding) {
            binding.commetatorDto = data
            Glide.with(itemView.context).load(data.profile).into(imgProfileImageInCommentator)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context , CommentatorActivity::class.java)
                intent.putExtra("INFO" , data)
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentatorViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemCommentatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CommentatorViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: CommentatorViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    companion object {
        val COMMENTATOR_DIFF_CALLBACK = object : DiffUtil.ItemCallback<CommentatorDto>() {
            override fun areItemsTheSame(oldItem: CommentatorDto, newItem: CommentatorDto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CommentatorDto, newItem: CommentatorDto): Boolean =
                oldItem == newItem
        }
    }

}