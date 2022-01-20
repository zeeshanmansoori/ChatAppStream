package com.google.chatappstream.presentation.channel

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.google.chatappstream.databinding.FragmentChannelBinding
import com.google.chatappstream.util.BaseFragment
import dagger.hilt.EntryPoint

@EntryPoint
class ChannelFragment :
    BaseFragment<FragmentChannelBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChannelBinding::inflate
}