package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentCommentatorSearchBinding
import com.greenstory.foreststory.databinding.FragmentFollowBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.adapter.FollowAdapter
import com.greenstory.foreststory.view.adapter.SearchCommentatorAdapter
import com.greenstory.foreststory.view.fragment.contents.CommentatorSearchFragmentArgs
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import kotlinx.coroutines.flow.collectLatest


class FollowFragment : Fragment() {

    lateinit var binding: FragmentFollowBinding
    lateinit var adapter: FollowAdapter
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_follow, container, false)
        binding.fragment = this@FollowFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        commentatorViewModel.getFollowCommentator()

        repeatOnStarted {
            commentatorViewModel.commentatorData.collectLatest { event ->
                handleEvent(event)
            }
        }
    }

    private fun getFollowCommentators(list : ArrayList<CommentatorDto>){
        adapter.submitList(list.map {
            it.copy()
        })
    }

    private fun initRecycler(){
        adapter = FollowAdapter()

        binding.recyclerFollow.adapter = adapter
        binding.recyclerFollow.layoutManager = LinearLayoutManager(contentsActivity)
    }

    private fun handleEvent(event: CommentatorViewModel.Event) = when (event) {
        is CommentatorViewModel.Event.FoundCommentators ->getFollowCommentators(event.foundCommentators)
        is CommentatorViewModel.Event.Success -> commentatorViewModel.getFollowCommentator()
        else -> {}
    }


}