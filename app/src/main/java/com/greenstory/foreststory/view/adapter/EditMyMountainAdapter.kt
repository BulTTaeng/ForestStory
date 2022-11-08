package com.greenstory.foreststory.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenstory.foreststory.databinding.ItemEditMountainBinding
import com.greenstory.foreststory.databinding.ItemMountainBinding
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.view.fragment.contents.MountainFragmentDirections
import com.greenstory.foreststory.view.fragment.contents.commentator.CommentatorProfileFragmentDirections

class EditMyMountainAdapter : ListAdapter<DetailLocationInfo, EditMyMountainAdapter.EditMyMountainViewHolder>(
    EditMyMountainAdapter.EDIT_MOUNTAIN_DIFF_CALLBACK
){

    lateinit var binding : ItemEditMountainBinding

    inner class EditMyMountainViewHolder(
        private val binding: ItemEditMountainBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: DetailLocationInfo) = with(binding) {

            Glide.with(itemView.context).load(data.image).into(binding.imgProgramImage)
            binding.txtProgramName.text = data.name

            itemView.setOnClickListener {
                val navController = Navigation.findNavController(itemView)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditMyMountainViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemEditMountainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return EditMyMountainViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: EditMyMountainViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    companion object {
        val EDIT_MOUNTAIN_DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailLocationInfo>() {
            override fun areItemsTheSame(oldItem: DetailLocationInfo, newItem: DetailLocationInfo): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: DetailLocationInfo, newItem: DetailLocationInfo): Boolean =
                oldItem == newItem
        }
    }
}