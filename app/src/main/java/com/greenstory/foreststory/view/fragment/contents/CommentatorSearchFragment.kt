package com.greenstory.foreststory.view.fragment.contents

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentCommentatorBinding
import com.greenstory.foreststory.databinding.FragmentCommentatorSearchBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.CommentatorAdapter
import com.greenstory.foreststory.view.adapter.SearchCommentatorAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import kotlinx.coroutines.flow.collectLatest


class CommentatorSearchFragment : Fragment() {

    lateinit var binding: FragmentCommentatorSearchBinding
    lateinit var adapter: SearchCommentatorAdapter
    lateinit var contentsActivity: ContentsActivity
    private val args by navArgs<CommentatorSearchFragmentArgs>()

    val commentatorViewModel: CommentatorViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentsActivity = context as ContentsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repeatOnStarted {
            commentatorViewModel.commentatorData.collectLatest { event ->
                handleEvent(event)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_commentator_search, container, false)
        binding.fragment = this@CommentatorSearchFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        commentatorViewModel.searchCommentators(args.searchString)
    }

    fun initRecycler() {
        adapter = SearchCommentatorAdapter()

        binding.recyclerCommentatorSearch.adapter = adapter

        val manager = LinearLayoutManager(contentsActivity)
        binding.recyclerCommentatorSearch.layoutManager = manager
    }

    fun updateSearchCommentators(foundCommentators: ArrayList<CommentatorDto>) {

        adapter.submitList(foundCommentators.map {
            it.copy()
        })
        binding.progressBarCommentatorSearch.visibility = View.GONE
    }

    fun btnSearchCommentator2(view: View){
        val str = binding.edtSearchCommentator2.text.toString()
        if (str.length < 2) {
            Toast.makeText(contentsActivity, "검색어를 2글자 이상 입력해 주세요", Toast.LENGTH_SHORT).show()
        } else {
            binding.progressBarCommentatorSearch.visibility = View.VISIBLE
            commentatorViewModel.searchCommentators(str)
        }
    }


    private fun handleEvent(event: CommentatorViewModel.Event) = when (event) {
        is CommentatorViewModel.Event.FoundCommentators ->updateSearchCommentators(event.foundCommentators)
        else -> {}
    }


}