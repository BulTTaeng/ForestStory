package com.greenstory.foreststory.view.fragment.contents.setting.add

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentAddProgramInfoBinding
import com.greenstory.foreststory.utility.event.repeatOnStarted
import com.greenstory.foreststory.view.activity.contents.setting.add.AddMountainProgramActivity
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel
import com.greenstory.foreststory.viewmodel.contents.setting.add.AddProgramViewModel
import kotlinx.coroutines.flow.collectLatest


class AddProgramInfoFragment : Fragment() {

    lateinit var binding: FragmentAddProgramInfoBinding
    lateinit var addMountainActivity: AddMountainProgramActivity
    val addProgramViewModel : AddProgramViewModel by activityViewModels()
    val settingViewModel : SettingViewModel by activityViewModels()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_program_info, container, false)
        binding.fragment = this@AddProgramInfoFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingViewModel.getUserName()
        binding.locations = addProgramViewModel.detailLocationInfo
        repeatOnStarted {
            addProgramViewModel.programEvent.collectLatest { event ->
                handleEventAddProgram(event)
            }
        }

        repeatOnStarted {
            settingViewModel.myInfo.collectLatest { event ->
                handleEventSetting(event)
            }
        }
    }

    fun uploaded(success : Boolean){
        if(success){
            val intent = Intent()
            intent.putExtra("EDITEDINFO", addProgramViewModel.detailLocationInfo)
            addMountainActivity.setResult(Activity.RESULT_OK , intent)
            addMountainActivity.finish()
        }else{
            Toast.makeText(addMountainActivity , getString(R.string.try_later) , Toast.LENGTH_SHORT).show()
        }
        binding.progressBarAddProgramInfo.visibility = View.GONE
    }

    fun makeSelectDialog(list : ArrayList<String> , title : String , view : TextView){
        val items = list.toArray(arrayOfNulls<String>(list.size))

        val builder = AlertDialog.Builder(addMountainActivity)
        builder.setTitle(title)
            .setItems(items,
                DialogInterface.OnClickListener { dialog, which ->
                    // ????????? ?????? 'which'??? ????????? position??? ???????????????.
                    view.text = list[which]
                })
        builder.show()
    }

    fun getName(name : String){
        binding.txtCommentatorName.text = name
        addProgramViewModel.detailLocationInfo.commentator = name
    }

    fun addLocation(view: View){
        val list = arrayListOf<String>("??????" , "??????" ,"??????, ??????")
        makeSelectDialog(list , "?????? ????????????" , binding.txtSelectedLocationInAdd)
    }

    fun addTime(view: View){
        val list = arrayListOf<String>("30???" , "1??????" , "1?????? ???" , "2??????" , "2?????? ???" , "3??????" , "3?????? ???" , "4??????" , "4?????? ???" , "5??????" , "5?????? ???" , "6??????" , "6?????? ???")
        makeSelectDialog(list , "?????? ????????????" , binding.txtSelectedTimeInAdd)
    }

    fun addTarget(view: View){
        val list = arrayListOf<String>("??????" , "?????????" ,"??????" , "??????")
        makeSelectDialog(list , "?????? ????????????" , binding.txtSelectedTargetInAdd)
    }

    fun btnAddProgramDone(view: View){
        val location = binding.txtSelectedLocationInAdd.text.toString()
        val time = binding.txtSelectedTimeInAdd.text.toString()
        val target = binding.txtSelectedTargetInAdd.text.toString()
        val commentator = binding.txtCommentatorName.text.toString()

        if(location.isEmpty()){
            Toast.makeText(addMountainActivity , "????????? ????????? ?????????" , Toast.LENGTH_SHORT).show()
            return
        }
        else if(time.isEmpty()){
            Toast.makeText(addMountainActivity , "????????? ????????? ?????????" , Toast.LENGTH_SHORT).show()
            return
        }
        else if(target.isEmpty()){
            Toast.makeText(addMountainActivity , "????????? ????????? ?????????" , Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBarAddProgramInfo.visibility = View.VISIBLE
        addProgramViewModel.detailLocationInfo.location = location
        addProgramViewModel.detailLocationInfo.time = time
        addProgramViewModel.detailLocationInfo.attendance = target
        addProgramViewModel.upLoadProgram()
    }

    fun handleEventAddProgram(event: AddProgramViewModel.Event) = when (event) {
        is AddProgramViewModel.Event.Success -> uploaded(event.success)
        else ->{}
    }

    fun handleEventSetting(event: SettingViewModel.Event) = when (event) {
        is SettingViewModel.Event.UserName -> getName(event.name)
        else ->{}
    }


}