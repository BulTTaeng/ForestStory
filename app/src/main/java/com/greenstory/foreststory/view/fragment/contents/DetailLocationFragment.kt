package com.greenstory.foreststory.view.fragment.contents

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentDetailLocationBinding
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.DetailLocationAdapter
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import kotlinx.coroutines.flow.collectLatest

class DetailLocationFragment : Fragment() , LifecycleOwner {
    lateinit var binding: FragmentDetailLocationBinding
    lateinit var adapter: DetailLocationAdapter
    val mountainViewModel: MountainViewModel by activityViewModels()
    private val args by navArgs<DetailLocationFragmentArgs>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_location, container, false)
        binding.fragment = this@DetailLocationFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarDetailLoc.visibility = View.GONE
        binding.txtLocation.text = args.dataInfo.name
        binding.txtExplainMountain.text = args.dataInfo.explain
        Glide.with(requireContext()).load(args.dataInfo.image).into(binding.imgMountainImageInDetail)
        getLocationData()
        initRecyclerView()
        repeatOnStarted {
            mountainViewModel.mountainData.collectLatest { event ->
                handleEvent(event)
            }
        }
    }

    fun getLocationData() {
        showSampleData(true)
        mountainViewModel.getDetailLoc(args.dataInfo.name)
    }

    private fun initRecyclerView() {
        if(requireContext().toString().contains("ContentsActivity")){
            adapter = DetailLocationAdapter(args.dataInfo , true)
        }
        else {
            adapter = DetailLocationAdapter(args.dataInfo , false)
        }

        binding.recyclerMountain.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMountain.adapter = adapter
        binding.recyclerMountain.isNestedScrollingEnabled = false
    }

    private fun updateLocations(mountainList : ArrayList<DetailLocationInfo>){
        adapter.submitList(mountainList.map {
            it.copy()
        })
        showSampleData(false)
    }

    fun handleEvent(event: MountainViewModel.Event) = when (event) {
        is MountainViewModel.Event.DetailLocations -> updateLocations(event.locs)
        else -> {}
    }

    private fun showSampleData(isLoading: Boolean) {
        if (isLoading) {
            binding.sflMountain.startShimmer()
            binding.sflMountain.visibility = View.VISIBLE
            binding.recyclerMountain.visibility = View.GONE
        } else {
            binding.sflMountain.stopShimmer()
            binding.sflMountain.visibility = View.GONE
            binding.recyclerMountain.visibility = View.VISIBLE
        }
    }

}