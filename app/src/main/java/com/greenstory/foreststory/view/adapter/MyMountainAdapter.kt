package com.greenstory.foreststory.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ItemMountainInProfileBinding
import com.greenstory.foreststory.model.contents.CommentatorPrograms
import com.greenstory.foreststory.model.contents.MountainDto


class MyMountainAdapter(val pLists : CommentatorPrograms) : ListAdapter<MountainDto, MyMountainAdapter.MyMountainViewHolder>(MOUNTAIN_DIFF_CALLBACK){

    lateinit var binding : ItemMountainInProfileBinding

    inner class MyMountainViewHolder(
        private val binding: ItemMountainInProfileBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: MountainDto) = with(binding) {
            binding.mountainDto = data

            Glide.with(itemView.context).load(data.image).into(binding.imgMountainImage)

            binding.btnShowEdit.setOnClickListener {
                showPopUp(it.context)
            }

            itemView.setOnClickListener {
                val navController = Navigation.findNavController(itemView)
                //navController.navigate(ProfileFragmentDirections.actionProfileFragmentToDetailLocationFragment(data, pLists)) //from myProfile
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMountainViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemMountainInProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyMountainViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: MyMountainViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    fun showPopUp(context: Context) {
        val popupMenu = PopupMenu(context, binding.btnShowEdit)
        popupMenu.inflate(R.menu.show_options_edit)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.delete -> {
                        return true
                    }
                    R.id.edit -> {
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
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