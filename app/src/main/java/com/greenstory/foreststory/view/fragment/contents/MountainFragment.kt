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
import androidx.recyclerview.widget.LinearLayoutManager
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentMountainBinding
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.MountainAdapter
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MountainFragment : Fragment() {
    lateinit var binding: FragmentMountainBinding
    lateinit var contentsActivity: ContentsActivity
    lateinit var adapter : MountainAdapter
    val mountainViewModel : MountainViewModel by activityViewModels()
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
        binding.button.setOnClickListener {
            getMountainWithDistance(37.1, 37.1)
        }
    }

    fun getMountainData(){
        CoroutineScope(Dispatchers.Main).launch {
            val success = mountainViewModel.getMountainData()

            if(success){
                initRecyclerView()
            }
            else{

            }
        }
    }

    fun initRecyclerView(){
        adapter = MountainAdapter()
        adapter.submitList(mountainViewModel.fetchMountainData().value)
        binding.recyclerMountain.layoutManager = LinearLayoutManager(contentsActivity)
        binding.recyclerMountain.adapter = adapter
        binding.progressBarMountain.visibility = View.GONE
    }

    fun getMountainWithDistance(lati : Double , longi : Double){

        try {
            var startLoc = Location("start")
            var distance = 0.0F
            startLoc.latitude = lati
            startLoc.longitude = longi

            mountainListDistance.clear()


            var endLoc = Location("end")

            for(it in mountainViewModel.fetchMountainData().value!!){
                endLoc.longitude = it.longitude
                endLoc.latitude = it.latitude

                distance = startLoc.distanceTo(endLoc)
                it.distance=distance
                mountainListDistance.add(it)
            }

            mountainListDistance.sortBy { it.distance }

            mountainViewModel.setMountainData(mountainListDistance)
            adapter.submitList(mountainViewModel.fetchMountainData().value)

        }catch (e : Exception){
            Log.d("Distance Exception" , e.toString())
        }
    }

}