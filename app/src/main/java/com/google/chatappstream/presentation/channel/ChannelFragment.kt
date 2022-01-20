package com.google.chatappstream.presentation.channel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.google.chatappstream.databinding.FragmentChannelBinding
import com.google.chatappstream.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.ui.channel.list.ChannelListView
import io.getstream.chat.android.ui.channel.list.adapter.viewholder.SwipeViewHolder
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChannelFragment :
    BaseFragment<FragmentChannelBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChannelBinding::inflate

    private val viewModel: ChannelViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = viewModel.getUser()
        if (user == null) {
            findNavController().popBackStack()
            return
        }

        val factory = ChannelListViewModelFactory(
            filter = Filters.and(
                Filters.eq("type", "messaging"),
                Filters.`in`("members", listOf(viewModel.getUser()?.id!!))
            ),
            sort = ChannelListViewModel.DEFAULT_SORT,
            limit = 30
        )

        val channelListViewModel: ChannelListViewModel by viewModels { factory }
        val channelListHeaderViewModel: ChannelListHeaderViewModel by viewModels()

        channelListHeaderViewModel.bindView(binding.channelListHeaderView, viewLifecycleOwner)
        channelListViewModel.bindView(binding.channelListView, viewLifecycleOwner)

        binding.channelListHeaderView.setOnUserAvatarClickListener {
            viewModel.logout()

            findNavController().navigateUp()
        }

        binding.channelListView.setChannelDeleteClickListener {
            Log.d("zeeshan", "onViewCreated: channel ${it.cid} $it")
            lifecycleScope.launch {
                viewModel.deleteChannel(it.id)
                showToast("channel deleted")
            }
        }

        binding.channelListHeaderView.setOnActionButtonClickListener {
            val action = ChannelFragmentDirections.actionChannelFragmentToUserListFragment()
            findNavController().navigate(action)
        }


        binding.channelListView.setChannelItemClickListener {
            val action = ChannelFragmentDirections.actionChannelFragmentToChatFragment(it.cid)
            findNavController().navigate(action)
        }


    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }




}