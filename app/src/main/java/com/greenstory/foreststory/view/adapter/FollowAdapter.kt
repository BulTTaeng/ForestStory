package com.greenstory.foreststory.view.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ItemCommentatorBinding
import com.greenstory.foreststory.databinding.ItemCommentatorFollowBinding
import com.greenstory.foreststory.databinding.ItemCommentatorSearchBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.utility.GlobalApplication
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.CommentatorAdapter.Companion.COMMENTATOR_DIFF_CALLBACK

class FollowAdapter : ListAdapter<CommentatorDto, FollowAdapter.FollowViewHolder>(COMMENTATOR_DIFF_CALLBACK){

    lateinit var binding : ItemCommentatorFollowBinding

    inner class FollowViewHolder(
        private val binding: ItemCommentatorFollowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: CommentatorDto) = with(binding) {
            binding.commetatorDto = data

            Glide.with(itemView.context).load(data.profile).into(imgCommentatorImageInFollow)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context , CommentatorActivity::class.java)
                intent.putExtra("INFO" , data)
                itemView.context.startActivity(intent)
            }

            binding.btnDeleteFollow.setOnClickListener {
                makeAlert(itemView.context , currentList[absoluteAdapterPosition].id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {

        binding = ItemCommentatorFollowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FollowViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    fun makeAlert(context : Context , id : String){
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.sure_delete))
            .setPositiveButton("아니요") { _: DialogInterface, i: Int ->
            }
            .setNegativeButton("네") { _: DialogInterface, i: Int ->
                (context as ContentsActivity).commentatorViewModel.stopFollow(id)
            }.setCancelable(false).show()
    }

}