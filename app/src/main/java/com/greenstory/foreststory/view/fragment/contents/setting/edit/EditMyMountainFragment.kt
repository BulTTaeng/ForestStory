package com.greenstory.foreststory.view.fragment.contents.setting.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentEditMyMountainBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.model.contents.CommentatorPrograms
import com.greenstory.foreststory.model.contents.DetailLocationInfo
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.view.activity.contents.setting.edit.EditMyMountainActivity
import com.greenstory.foreststory.view.adapter.EditMyMountainAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import kotlinx.coroutines.flow.collectLatest

class EditMyMountainFragment : Fragment() {

    lateinit var binding: FragmentEditMyMountainBinding
    lateinit var adapter: EditMyMountainAdapter
    lateinit var editMyMountainActivity: EditMyMountainActivity
    val mountainViewModel: MountainViewModel by activityViewModels()
    val commentatorViewModel : CommentatorViewModel by activityViewModels()
    var commentatorPrograms = CommentatorPrograms()

    val editedMountainResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            commentatorViewModel.getMyCommentator()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        editMyMountainActivity = context as EditMyMountainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_my_mountain, container, false)
        binding.fragment = this@EditMyMountainFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtMountainName.text = editMyMountainActivity.name
        commentatorViewModel.getMyCommentator()
        initRecycler()

        repeatOnStarted {
            commentatorViewModel.commentatorData.collectLatest { event ->
                handleEvent(event)
            }
        }

        repeatOnStarted {
            mountainViewModel.mountainData.collectLatest { event ->
                handleEventOfMountainViewModel(event)

            }
        }

    }

    private fun initRecycler(){
        adapter = EditMyMountainAdapter()
        binding.recyclerEditMyMountain.layoutManager = GridLayoutManager(editMyMountainActivity , 2)
        binding.recyclerEditMyMountain.adapter = adapter
        //binding.recyclerEditMyMountain.isNestedScrollingEnabled = false

    }

    private fun updateRecycler(myProgramList: ArrayList<DetailLocationInfo>){
        adapter.submitList(myProgramList.map {
            it.copy()
        })

        binding.progressBarEditMyMountainFragment.visibility = View.GONE
    }

    private fun getProgramLists(info : CommentatorDto?){
        if(info != null) {
            for (it in info.mountains) {
                val programs = info.mountain[it]

                if (programs != null) {
                    for (p in programs) {
                        commentatorPrograms.detailProgramLists.add(p)
                    }
                }
            }
            mountainViewModel.getDetailLocContains(
                editMyMountainActivity.name,
                commentatorPrograms.detailProgramLists
            )
        }
    }

    fun AddProgram(view : View){
        val intent = Intent(editMyMountainActivity , AddMountainProgramActivity::class.java)
        intent.putExtra("MOUNTAINNAME" , editMyMountainActivity.name)
        intent.putExtra("DETAILINFO" , DetailLocationInfo())
        editedMountainResult.launch(intent)
    }

    private fun handleEvent(event: CommentatorViewModel.Event) = when (event) {
        is CommentatorViewModel.Event.OneCommentator ->getProgramLists(event.oneCommentator)
        else -> {}
    }

    private fun handleEventOfMountainViewModel(event: MountainViewModel.Event) = when(event){
        is MountainViewModel.Event.DetailLocations -> updateRecycler(event.locs)
        else -> {}
    }

}