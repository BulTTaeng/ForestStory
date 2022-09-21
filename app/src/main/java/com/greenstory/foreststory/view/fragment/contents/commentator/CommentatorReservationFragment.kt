package com.greenstory.foreststory.view.fragment.contents.commentator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentCommentatorProfileBinding
import com.greenstory.foreststory.databinding.FragmentCommentatorReservationBinding
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity
import com.greenstory.foreststory.view.adapter.CommentatorMountainAdapter
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel


class CommentatorReservationFragment : Fragment() {

    lateinit var binding: FragmentCommentatorReservationBinding
    lateinit var commentatorActivity: CommentatorActivity

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_commentator_reservation, container, false)
        binding.fragment = this@CommentatorReservationFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.commentatorInfo = commentatorActivity.commentatorDto
        Glide.with(commentatorActivity).load(commentatorActivity.commentatorDto.profile).into(binding.imgCommentatorImageInReservation)
    }

    fun btnBackButtonInCommentatorReservation(view: View){
        findNavController().navigate(R.id.commentatorProfileFragment)
    }

    fun btnReservation(view: View){

    }
}