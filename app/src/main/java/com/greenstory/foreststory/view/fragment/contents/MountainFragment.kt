package com.greenstory.foreststory.view.fragment.contents

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentMountainBinding
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.MountainAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MountainFragment : Fragment(), LifecycleOwner {
    lateinit var binding: FragmentMountainBinding
    lateinit var contentsActivity: ContentsActivity
    lateinit var adapter: MountainAdapter
    val mountainViewModel: MountainViewModel by activityViewModels()
    var mountainListDistance = ArrayList<MountainDto>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentsActivity = context as ContentsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mountain, container, false)
        binding.fragment = this@MountainFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMountainData()
        initRecyclerView()
        repeatOnStarted {
            mountainViewModel.mountainData.collectLatest { event ->
                binding.progressBarMountain.visibility = View.VISIBLE
                handleEvent(event)
            }
        }

        binding.button.setOnClickListener {
                binding.progressBarMountain.visibility = View.VISIBLE
                mountainViewModel.getMountainWithDistance(37.1, 37.1)

        }
    }

    fun getMountainData() {
        binding.progressBarMountain.visibility = View.VISIBLE
        mountainViewModel.getMountainData()
    }

    fun initRecyclerView() {
        adapter = MountainAdapter()
        binding.recyclerMountain.layoutManager = LinearLayoutManager(contentsActivity)
        binding.recyclerMountain.adapter = adapter
    }

    fun updateMountain(mountainList : ArrayList<MountainDto>){
        adapter.submitList(mountainList.map {
            it.copy()
        })
        binding.progressBarMountain.visibility = View.GONE
    }

    fun handleEvent(event: MountainViewModel.Event) = when (event) {
        is MountainViewModel.Event.Mountains -> updateMountain(event.mountains)
    }

}