package com.greenstory.foreststory.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenstory.foreststory.databinding.ItemDetailLocationBinding
import com.greenstory.foreststory.databinding.ItemMountainBinding
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity

class DetailLocationAdapter(val mountaindto : MountainDto) : ListAdapter<DetailLocationInfo, DetailLocationAdapter.DetailLocViewHolder>(DETAILLOC_DIFF_CALLBACK){

    lateinit var binding : ItemDetailLocationBinding

    inner class DetailLocViewHolder(
        private val binding: ItemDetailLocationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: DetailLocationInfo) = with(binding) {
            binding.locations = data

            Glide.with(itemView.context).load(data.image).into(binding.imgDetailLocation)

            itemView.setOnClickListener {
                val intent = Intent(it.context , AudioPlayerActivity::class.java)
                intent.putExtra("MOUNTAIN" , mountaindto)
                intent.putExtra("DETAIL", data)
                it.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailLocViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemDetailLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DetailLocViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: DetailLocViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    companion object {
        val DETAILLOC_DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailLocationInfo>() {
            override fun areItemsTheSame(oldItem: DetailLocationInfo, newItem: DetailLocationInfo): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: DetailLocationInfo, newItem: DetailLocationInfo): Boolean =
                oldItem == newItem
        }
    }

}