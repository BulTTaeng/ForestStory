package com.greenstory.foreststory.view.fragment.contents

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentCommentatorBinding
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.CommentatorAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CommentatorFragment : Fragment() {

    lateinit var binding : FragmentCommentatorBinding
    lateinit var adapter : CommentatorAdapter
    lateinit var contentsActivity : ContentsActivity

    val commentatorViewModel  :  CommentatorViewModel by activityViewModels()

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
        binding.progressBarCommentator.visibility = View.GONE


        commentatorViewModel.getCommentators()
        observeData()
        initRecycler()

        binding.swipeCommetator.setOnRefreshListener {
            binding.progressBarCommentator.visibility = View.VISIBLE
            commentatorViewModel.getCommentators()
        }
    }

    fun initRecycler(){
        adapter =CommentatorAdapter()

        binding.recyclerCommentator.adapter =adapter

        val manager  = LinearLayoutManager(contentsActivity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerCommentator.layoutManager = manager
    }

    fun observeData() {
        commentatorViewModel.commentatorData.observe(viewLifecycleOwner) {
            adapter.submitList(commentatorViewModel.commentatorData.value?.map {
                it.copy()
            })
            binding.swipeCommetator.isRefreshing = false
            binding.progressBarCommentator.visibility = View.GONE
        }
    }

    fun btnSearchCommentator(view:View){

    }
}