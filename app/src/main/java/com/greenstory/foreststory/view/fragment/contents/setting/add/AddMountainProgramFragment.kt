package com.greenstory.foreststory.view.fragment.contents.setting.add

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
import com.greenstory.foreststory.databinding.FragmentAddMountainProgramBinding
import com.greenstory.foreststory.model.contents.CommentatorPrograms
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.view.adapter.MountainAdapter
import com.greenstory.foreststory.view.fragment.contents.CommentatorSearchFragmentArgs
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.add.AddProgramViewModel
import kotlinx.coroutines.flow.collectLatest


class AddMountainProgramFragment : Fragment() {

    lateinit var binding: FragmentAddMountainProgramBinding
    lateinit var addMountainActivity: AddMountainProgramActivity
    lateinit var adapter : MountainAdapter
    var commentatorPrograms = CommentatorPrograms()
    val mountainViewModel : MountainViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addMountainActivity = context as AddMountainProgramActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_mountain_program, container, false)
        binding.fragment = this@AddMountainProgramFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSampleData(false)
        initRecyclerView()
        repeatOnStarted {
            mountainViewModel.mountainData.collectLatest { event ->
                handleEvent(event)
            }
        }

    }

    fun initRecyclerView() {
        adapter = MountainAdapter(3 , CommentatorPrograms())
        binding.recyclerSearchMountain.layoutManager = LinearLayoutManager(addMountainActivity)
        binding.recyclerSearchMountain.adapter = adapter
    }

    private fun updateMountain(mountainList : ArrayList<MountainDto>){
        adapter.submitList(mountainList.map {
            it.copy()
        })

        showSampleData(false)
    }

    private fun showSampleData(isLoading: Boolean) {
        if (isLoading) {
            binding.sflSearchMountain.startShimmer()
            binding.sflSearchMountain.visibility = View.VISIBLE
            binding.recyclerSearchMountain.visibility = View.GONE
        } else {
            binding.sflSearchMountain.stopShimmer()
            binding.sflSearchMountain.visibility = View.GONE
            binding.recyclerSearchMountain.visibility = View.VISIBLE
        }
    }

    fun btnSearchMountain(view: View){
        val searchStr = binding.edtSearchMountain.text.toString()

        if(searchStr.isEmpty()) return
        else if(searchStr.length <2){
            Toast.makeText(addMountainActivity , "검색어는 두글자 이상이여야 합니다" ,Toast.LENGTH_SHORT).show()
            return
        }

        showSampleData(true)
        mountainViewModel.getMountainDataContain(binding.edtSearchMountain.text.toString())
    }

    fun handleEvent(event: MountainViewModel.Event) = when (event) {
        is MountainViewModel.Event.Mountains -> updateMountain(event.mountains)
        else ->{}
    }

}