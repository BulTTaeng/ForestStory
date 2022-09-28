package com.greenstory.foreststory.view.fragment.contents

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentCommentatorBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.CommentatorAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


class CommentatorFragment : Fragment() {

    lateinit var binding: FragmentCommentatorBinding
    lateinit var adapter: CommentatorAdapter
    lateinit var contentsActivity: ContentsActivity

    val commentatorViewModel: CommentatorViewModel by activityViewModels()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_commentator, container, false)
        binding.fragment = this@CommentatorFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        commentatorViewModel.getCommentators()
        //observeData()
        initRecycler()

        repeatOnStarted {
            commentatorViewModel.commentatorData.collectLatest { event ->
                handleEvent(event)
            }
        }
    }

    fun initRecycler() {
        adapter = CommentatorAdapter()

        binding.recyclerCommentator.adapter = adapter

        val manager = LinearLayoutManager(contentsActivity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerCommentator.layoutManager = manager
    }

    fun updateCommentators(commentators: ArrayList<CommentatorDto>) {
        adapter.submitList(commentators.map {
            it.copy()
        })
        binding.progressBarCommentator.visibility = View.GONE
    }



    private fun handleEvent(event: CommentatorViewModel.Event) = when (event) {
        is CommentatorViewModel.Event.Commentators -> updateCommentators(event.commentators)
        else -> {}
    }

    fun btnSearchCommentator(view: View) {
        findNavController().navigate(CommentatorFragmentDirections.actionCommentatorFragmentToCommentatorSearchFragment(binding.edtSearchCommentator.text.toString()))

    }

}