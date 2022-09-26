package com.greenstory.foreststory.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenstory.foreststory.databinding.ItemCommentatorBinding
import com.greenstory.foreststory.databinding.ItemCommentatorSearchBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity
import com.greenstory.foreststory.view.adapter.CommentatorAdapter.Companion.COMMENTATOR_DIFF_CALLBACK

class SearchCommentatorAdapter : ListAdapter<CommentatorDto, SearchCommentatorAdapter.SearchCommentatorViewHolder>(COMMENTATOR_DIFF_CALLBACK){

    lateinit var binding : ItemCommentatorSearchBinding
    var hashTags : String =""

    inner class SearchCommentatorViewHolder(
        private val binding: ItemCommentatorSearchBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: CommentatorDto) = with(binding) {
            binding.commetatorDto = data
            hashTags = ""
            Glide.with(itemView.context).load(data.profile).into(imgCommentatorImageInSearch)

            for(it in data.hashTag.split(" ")){
                hashTags += "#$it "
            }
            txtCommentatorHashTagInSearch.text = hashTags

            itemView.setOnClickListener {
                val intent = Intent(itemView.context , CommentatorActivity::class.java)
                intent.putExtra("INFO" , data)
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCommentatorViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemCommentatorSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SearchCommentatorViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: SearchCommentatorViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

}