package com.greenstory.foreststory.view.fragment.contents.setting.add

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentAddMountainProgramBinding
import com.greenstory.foreststory.databinding.FragmentAddProgramBinding
import com.greenstory.foreststory.model.contents.CommentatorPrograms
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.view.adapter.MountainAdapter
import com.greenstory.foreststory.view.fragment.contents.CommentatorSearchFragmentArgs
import com.greenstory.foreststory.viewmodel.contents.MountainViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.add.AddProgramViewModel


class AddProgramFragment : Fragment() {

    lateinit var binding: FragmentAddProgramBinding
    lateinit var addMountainActivity: AddMountainProgramActivity
    val addProgramViewModel : AddProgramViewModel by activityViewModels()
    var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private val args by navArgs<AddProgramFragmentArgs>()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_program, container, false)
        binding.fragment = this@AddProgramFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addProgramViewModel.mountainName = args.mountainName
        Glide.with(addMountainActivity).load(R.drawable.search_image).into(binding.btnAddProgramImage)
        registerPhotoPicker()
    }

    fun registerPhotoPicker(){

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                addProgramViewModel.detailLocationInfo.image = uri.toString()
                Glide.with(addMountainActivity).load(addProgramViewModel.detailLocationInfo.image).into(binding.btnAddProgramImage)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    fun btnAddProgramImage(view: View){
        pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun btnToAddProgramInfo(view: View){

        val programName = binding.edtProgramNameAdd.text.toString()
        val programExplain = binding.edtProgramExplainAdd.text.toString()

        if(addProgramViewModel.detailLocationInfo.image.isEmpty()){
            Toast.makeText(addMountainActivity ,"이미지를 업로드 해 주세요" , Toast.LENGTH_SHORT).show()
            return
        }
        else if(programName.isEmpty()){
            Toast.makeText(addMountainActivity ,"프로그램 이름을 입력해 주세요" , Toast.LENGTH_SHORT).show()
            return
        }
        else if(programExplain.isEmpty()){
            Toast.makeText(addMountainActivity ,"프로그램 설명을 입력해 주세요" , Toast.LENGTH_SHORT).show()
            return
        }

        addProgramViewModel.detailLocationInfo.name = programName
        addProgramViewModel.detailLocationInfo.explain = programExplain
        findNavController().navigate(R.id.addProgramInfoFragment)
    }


}