package com.greenstory.foreststory.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greenstory.foreststory.databinding.ItemSettingBinding

class SettingAdapter: ListAdapter<String, SettingAdapter.SettingViewHolder>(SETTING_DIFF_CALLBACK){

    lateinit var binding : ItemSettingBinding
    private lateinit var onClickListener: (String) -> Unit

    inner class SettingViewHolder(
        private val binding: ItemSettingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: String) = with(binding) {
            txtSettingStr.text = data
        }

        fun bindView(data : String){
            binding.root.setOnClickListener {
                onClickListener(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        //val view = LayoutInflater.from(parent.context)
        //   .inflate(R.layout.cardview_farm, parent, false)
        binding = ItemSettingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SettingViewHolder(binding)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.bindData(currentList[position])
        holder.bindView(currentList[position])
    }

    fun setOnClickListener(data: List<String>, onClickListener: (String) -> Unit) {
        this.onClickListener = onClickListener
        submitList(data)
    }

    companion object {
        val SETTING_DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }

}