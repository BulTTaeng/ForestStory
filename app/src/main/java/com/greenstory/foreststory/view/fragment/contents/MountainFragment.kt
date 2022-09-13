package com.greenstory.foreststory.view.fragment.contents

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentMountainBinding
import com.greenstory.foreststory.model.audio.AudioDto
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.model.contents.MountainEntity
import com.greenstory.foreststory.utility.DescriptionAdapter
import com.greenstory.foreststory.view.activity.audio.AudioPlayerActivity
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.MountainAdapter
import com.greenstory.foreststory.view.adapter.SettingAdapter
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


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