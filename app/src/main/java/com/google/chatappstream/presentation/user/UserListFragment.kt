package com.google.chatappstream.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.chatappstream.databinding.FragmentUserListBinding
import com.google.chatappstream.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUserListBinding::inflate

    private val viewModel: UserListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserListAdapter { selectedUserId ->
            lifecycleScope.launch {
                viewModel.createNewChannel(selectedUserId)
            }
        }
        binding.usersRecyclerView.setHasFixedSize(true)
        binding.usersRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.users.collectLatest {
                showHideProgressBar(true)
                adapter.submitList(it)
                showHideProgressBar(false)
            }
        }
        subscribeToEvents()
    }


    private fun subscribeToEvents() {
        lifecycleScope.launch {
            viewModel.userListState.collectLatest { event ->
                when (event) {
                    is UserListViewModel.UserListEvent.Error -> {
                        showHideProgressBar(false)
                        displayToast(event.error.toString())
                    }
                    is UserListViewModel.UserListEvent.ChannelCreated -> {
                        displayToast("channel created")
                        showHideProgressBar(false)
                        navigateToChatFragment(event.channelId)
                    }
                }
            }
        }
    }

    private fun showHideProgressBar(show: Boolean) {
        binding.progressBar.isVisible = show
    }

    private fun displayToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToChatFragment(channelId: String) {
        val direction = UserListFragmentDirections.actionUserListFragmentToChatFragment(channelId)
        findNavController().navigate(direction)
    }
}