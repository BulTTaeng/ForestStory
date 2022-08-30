package com.greenstory.foreststory.view.fragment.contents.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.strictmode.FragmentStrictMode
import androidx.fragment.app.viewModels
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.FragmentSettingBinding
import com.greenstory.foreststory.viewmodel.contents.setting.SettingViewModel

class SettingFragment : Fragment() {

    lateinit var binding : FragmentSettingBinding
    val settingViewModel : SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting, container, false)
        binding.fragment = this@SettingFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            settingViewModel.logOut()
        }
    }
}