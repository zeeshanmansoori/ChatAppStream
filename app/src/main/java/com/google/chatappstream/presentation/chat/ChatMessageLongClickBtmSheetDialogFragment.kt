package com.google.chatappstream.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.chatappstream.databinding.ChatMsgLongClickBtmSheetBinding
import com.google.chatappstream.util.log
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Message

@AndroidEntryPoint
class ChatMessageLongClickBtmSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun getInstance(msg: Message): ChatMessageLongClickBtmSheetDialogFragment {
            return ChatMessageLongClickBtmSheetDialogFragment().apply {
                globalMsg = msg
            }
        }
    }

    private var _binding: ChatMsgLongClickBtmSheetBinding? = null
    private val binding get() = _binding!!
    private var globalMsg: Message? = null

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
                messageListViewModel.onEvent(MessageListViewModel.Event.DeleteMessage(globalMsg!!))
            }
            dismiss()
        }.onFailure {
            log("error while deleting \n $it")
            dismiss()
        }
    }

    private fun markStar() {
        kotlin.runCatching {
            (parentFragment as ChatFragment).apply {
                messageListViewModel.onEvent(MessageListViewModel.Event.DeleteMessage(globalMsg!!))
            }
        }
    }
}