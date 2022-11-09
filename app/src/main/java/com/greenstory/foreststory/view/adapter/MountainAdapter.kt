package com.greenstory.foreststory.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.greenstory.foreststory.databinding.ItemMountainBinding
import com.greenstory.foreststory.model.contents.CommentatorPrograms
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.model.contents.MountainEntity
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.view.fragment.contents.CommentatorFragmentDirections
import com.greenstory.foreststory.view.fragment.contents.MountainFragment
import com.greenstory.foreststory.view.fragment.contents.MountainFragmentDirections
import com.greenstory.foreststory.view.fragment.contents.commentator.CommentatorProfileFragmentDirections
import com.greenstory.foreststory.view.fragment.contents.setting.ProfileFragmentDirections
import com.greenstory.foreststory.view.fragment.contents.setting.add.AddMountainProgramFragment
import com.greenstory.foreststory.view.fragment.contents.setting.add.AddMountainProgramFragmentDirections


class MountainAdapter(val fromWhere : Int , val pLists : CommentatorPrograms) : ListAdapter<MountainDto, MountainAdapter.MountainViewHolder>(MOUNTAIN_DIFF_CALLBACK){

    lateinit var binding : ItemMountainBinding

    inner class MountainViewHolder(
        private val binding: ItemMountainBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: MountainDto) = with(binding) {
            binding.mountainDto = data

            Glide.with(itemView.context).load(data.image).into(binding.imgMountainImage)

            itemView.setOnClickListener {
//                val intent = Intent(it.context , AudioPlayerActivity::class.java)
//                intent.putExtra("MOUNTAIN" , data)
//                it.context.startActivity(intent)
                val navController = Navigation.findNavController(itemView)

                when(fromWhere){
                    1 -> navController.navigate(MountainFragmentDirections.actionMountainFragmentToDetailLocationFragment(data,pLists)) //From MountainPage
                    2 -> navController.navigate(CommentatorProfileFragmentDirections.actionCommentatorProfileFragmentToDetailLocationFragment2(data , pLists)) // from CommentatorPage
                    3 -> navController.navigate(AddMountainProgramFragmentDirections.actionAddMountainFragmentToAddProgramFragment(data.name))

                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MountainViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemMountainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MountainViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: MountainViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    companion object {
        val MOUNTAIN_DIFF_CALLBACK = object : DiffUtil.ItemCallback<MountainDto>() {
            override fun areItemsTheSame(oldItem: MountainDto, newItem: MountainDto): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: MountainDto, newItem: MountainDto): Boolean =
                oldItem == newItem
        }
    }

}