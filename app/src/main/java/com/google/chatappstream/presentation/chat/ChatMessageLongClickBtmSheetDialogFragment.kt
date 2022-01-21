package com.google.chatappstream.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.chatappstream.databinding.ChatMsgLongClickBtmSheetBinding
import com.google.chatappstream.util.Constants
import com.google.chatappstream.util.log
import com.google.chatappstream.util.userList
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatMessageLongClickBtmSheetDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var chatClient: ChatClient

    companion object {
        fun getInstance(msg: MessageListItem.MessageItem): ChatMessageLongClickBtmSheetDialogFragment {
            return ChatMessageLongClickBtmSheetDialogFragment().apply {
                globalMsg = msg
            }
        }
    }

    private var _binding: ChatMsgLongClickBtmSheetBinding? = null
    private val binding get() = _binding!!
    private var globalMsg: MessageListItem.MessageItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChatMsgLongClickBtmSheetBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            deleteMsg.setOnClickListener {
                log("globalMsg $globalMsg")
                deleteMsg()
            }
            startMsg.setOnClickListener { }
        }
    }

    private fun deleteMsg() {
        kotlin.runCatching {
            (parentFragment as ChatFragment).apply {
                messageListViewModel.onEvent(MessageListViewModel.Event.DeleteMessage(globalMsg?.message!!))
            }
            dismiss()
        }.onFailure {
            log("error while deleting \n $it")
            dismiss()
        }
    }

    private fun markUnMarkStar(): Boolean {
        var star = false
        kotlin.runCatching {

            val userId = chatClient.getCurrentUser()?.id!!
            val msg = globalMsg!!.message.apply {
                val oldList = userList
                star = oldList.contains(userId)
                val newLs = mutableListOf<String>()
                newLs.addAll(oldList)

                if (star)
                    newLs.remove(userId)
                else
                    newLs.add(userId)

                extraData = mutableMapOf(Constants.STAR_BY_USERS to newLs)
            }

            (parentFragment as ChatFragment).apply {
                lifecycleScope.launch {
                    val result = chatClient.updateMessage(msg)
                }
            }
        }

        return !star
    }


}