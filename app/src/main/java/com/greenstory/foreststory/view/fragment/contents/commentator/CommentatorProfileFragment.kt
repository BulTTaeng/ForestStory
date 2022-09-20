package com.greenstory.foreststory.view.fragment.contents.commentator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentCommentatorProfileBinding
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity
import com.greenstory.foreststory.view.adapter.CommentatorMountainAdapter
import com.greenstory.foreststory.view.adapter.MountainAdapter
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel


class CommentatorProfileFragment : Fragment() {

    lateinit var binding: FragmentCommentatorProfileBinding
    lateinit var commentatorActivity: CommentatorActivity
    lateinit var adapter : CommentatorMountainAdapter
    val mountainViewModel: MountainViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        commentatorActivity = context as CommentatorActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_commentator_profile, container, false)
        binding.fragment = this@CommentatorProfileFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.commentatorInfo = commentatorActivity.commentatorDto
        Glide.with(commentatorActivity).load(commentatorActivity.commentatorDto.profile).into(binding.imgCommentatorImage)

        getMountainData(commentatorActivity.commentatorDto.mountain)
        observeData()
        initRecycler()

    }

    fun getMountainData(list : ArrayList<String>) {
        binding.progressBarCommentatorProfile.visibility = View.VISIBLE
        mountainViewModel.getMountainDataContain(list)
    }

    fun observeData() {
        mountainViewModel.mountainData.observe(viewLifecycleOwner) {
            adapter.submitList(mountainViewModel.mountainData.value?.map {
                it.copy()
            })
            binding.progressBarCommentatorProfile.visibility = View.GONE
        }
    }

    fun initRecycler(){
        adapter = CommentatorMountainAdapter()
        binding.recyclerCommentatorMountain.layoutManager = LinearLayoutManager(commentatorActivity)
        binding.recyclerCommentatorMountain.adapter = adapter
    }


    fun btnBackButtonInCommentator(view: View){
        activity?.finish()
    }

    fun btnOfflineReservation(view: View){
        findNavController().navigate(R.id.action_commentatorProfileFragment_to_commentatorReservationFragment)
    }


}