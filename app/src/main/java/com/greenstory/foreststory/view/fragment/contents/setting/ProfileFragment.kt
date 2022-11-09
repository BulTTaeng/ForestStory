package com.greenstory.foreststory.view.fragment.contents.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentProfileBinding
import com.greenstory.foreststory.model.contents.CommentatorDto
import com.greenstory.foreststory.model.contents.CommentatorPrograms
import com.greenstory.foreststory.model.contents.MountainDto
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.ContentsActivity
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.view.adapter.MyMountainAdapter
import com.greenstory.foreststory.viewmodel.contents.CommentatorViewModel
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import kotlinx.coroutines.flow.collectLatest


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    val commentatorViewModel : CommentatorViewModel by activityViewModels()
    val mountainViewModel : MountainViewModel by activityViewModels()
    lateinit var contentsActivity: ContentsActivity
    lateinit var adapter : MyMountainAdapter
    var commentatorPrograms = CommentatorPrograms()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.fragment = this@ProfileFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(commentatorViewModel.userInfo != null) {

            binding.txtProfile.text = commentatorViewModel.userInfo?.nickName
            binding.txtFollowNum.text = commentatorViewModel.userInfo?.likePerson?.size.toString()

            if (commentatorViewModel.userInfo!!.admin) {
                showCommentatorPage()
            } else {
                showUserPage()
            }
        }
        else{
            activity?.onBackPressed()
        }

    }

    private fun showCommentatorPage(){
        binding.userViewGroup.visibility = View.GONE
        binding.commentatorViewGroup.visibility = View.VISIBLE
        commentatorViewModel.getMyCommentator()

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

    private fun showUserPage(){
        binding.commentatorViewGroup.visibility = View.GONE
        binding.imgVerified.visibility = View.GONE
        binding.txtHashTagCommentator.visibility = View.GONE
        binding.userViewGroup.visibility = View.VISIBLE
        binding.txtExplainCommentator.text = commentatorViewModel.userInfo?.explain
        binding.txtCommentatorName.text = commentatorViewModel.userInfo?.name
        Glide.with(contentsActivity).load(commentatorViewModel.userInfo?.profile).into(binding.imgCommentatorImage)
    }

    fun initRecycler(list: ArrayList<MountainDto>) {
        adapter = MyMountainAdapter()
        binding.recyclerMyMountain.layoutManager = LinearLayoutManager(contentsActivity)
        binding.recyclerMyMountain.adapter = adapter

        adapter.submitList(list.map {
            it.copy()
        })
    }

    fun updateCommentatorInfo(info : CommentatorDto?){
        if(info == null){
            Toast.makeText(contentsActivity , getString(R.string.try_later) , Toast.LENGTH_SHORT).show()
            return
        }

        Glide.with(contentsActivity).load(info.profile).into(binding.imgCommentatorImage)

        binding.txtCommentatorName.text = info.name
        binding.txtExplainCommentator.text = info.explain
        binding.txtFollowerNum.text = info.likedNum.toString()

        mountainViewModel.getMountainDataContain(info.mountains)

        splitHash(info)
        getProgramLists(info)

    }

    private fun splitHash(info : CommentatorDto?){
        var hashTags = ""
        for(it in info!!.hashTag.split(" ")){
            hashTags += "#$it "
        }
        binding.txtHashTagCommentator.text = hashTags
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
            binding.txtContentsNum.text = commentatorPrograms.detailProgramLists.size.toString()
        }
    }

    fun btnSettingInProfile(view: View){
        findNavController().navigate(R.id.settingFragment)
    }

    fun txtFollow(view: View){
        findNavController().navigate(R.id.followFragment)
    }

    fun btnAddMyMountain(view: View){
        val intent = Intent(contentsActivity , AddMountainProgramActivity::class.java)
        startActivity(intent)
    }

    private fun updateRecycler(list: ArrayList<MountainDto>){
        initRecycler(list)
    }

    private fun handleEvent(event: CommentatorViewModel.Event) = when (event) {
        is CommentatorViewModel.Event.OneCommentator ->updateCommentatorInfo(event.oneCommentator)
        else -> {}
    }

    private fun handleEventOfMountainViewModel(event: MountainViewModel.Event) = when(event){
        is MountainViewModel.Event.Mountains -> updateRecycler(event.mountains)
        else -> {}
    }
}