package com.greenstory.foreststory.view.fragment.contents.commentator

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentCommentatorReservationBinding
import com.greenstory.foreststory.view.activity.contents.CommentatorActivity
import com.greenstory.foreststory.viewmodel.audio.AudioViewModel
import java.text.SimpleDateFormat
import java.util.*


class CommentatorReservationFragment : Fragment() {

    lateinit var binding: FragmentCommentatorReservationBinding
    lateinit var commentatorActivity: CommentatorActivity
    val audioViewModel : AudioViewModel by viewModels()

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
        activity?.onBackPressed()
    }

    fun btnReservation(view: View){

    }

    fun txtSelectDay(view: View){
        val datepickercalendar = Calendar.getInstance()
        val year = datepickercalendar.get(Calendar.YEAR)
        val month = datepickercalendar.get(Calendar.MONTH)
        val day = datepickercalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            commentatorActivity,
            R.style.MySpinnerDatePickerStyle,
            { _, year, monthOfYear, dayOfMonth ->
//                  ?????? 0?????? ???????????? 1??? ??????????????????
                val month = monthOfYear + 1
//                   ????????? ????????? ????????? ????????? ?????? calendar
                val calendar = Calendar.getInstance()
//                    ????????? ?????? ??????
                calendar.set(year, monthOfYear, dayOfMonth)
                val date = calendar.time
                val simpledateformat = SimpleDateFormat("EEEE", Locale.getDefault())
                val dayName: String = simpledateformat.format(date)

                binding.txtSelectedDay.text = "$year.$month.$dayOfMonth ($dayName)"
                binding.txtSelectedDay.setTextColor(Color.BLACK)

            },
            year,
            month,
            day
        )
//           ?????? ????????? ?????? ?????? ?????????
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000;
        dpd.show()
    }

    fun txtSelectLocation(view: View){

        val items = commentatorActivity.commentatorDto.mountains.toArray(arrayOfNulls<String>(commentatorActivity.commentatorDto.mountain.size))

        val builder = AlertDialog.Builder(commentatorActivity)
        builder.setTitle("?????? ????????????")
            .setItems(items,
                DialogInterface.OnClickListener { dialog, which ->
                    // ????????? ?????? 'which'??? ????????? position??? ???????????????.
                    binding.txtSelectedLocation.text = commentatorActivity.commentatorDto.mountains[which]
                    binding.txtSelectedLocation.setTextColor(Color.BLACK)
                })
        // ?????????????????? ????????????
        builder.show()
    }

    fun txtSelectedProgram(view : View){

        if(binding.txtSelectedLocation.text == "?????? ?????????"){
            Toast.makeText(commentatorActivity , "????????? ?????? ???????????????" , Toast.LENGTH_SHORT).show()
            return
        }

        val items = commentatorActivity.commentatorDto.mountain[binding.txtSelectedLocation.text]!!.toArray(arrayOfNulls<String>(commentatorActivity.commentatorDto.mountain.size))

        val builder = AlertDialog.Builder(commentatorActivity)
        builder.setTitle("?????? ????????????")
            .setItems(items,
                DialogInterface.OnClickListener { dialog, which ->
                    // ????????? ?????? 'which'??? ????????? position??? ???????????????.
                    //Color.parseColor("#bdbdbd")
                    binding.txtSelectedProgram.text = items[which]
                    binding.txtSelectedProgram.setTextColor(Color.BLACK)
                })
        // ?????????????????? ????????????
        builder.show()
    }
}