package com.google.chatappstream.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.google.chatappstream.databinding.FragmentChatBinding
import com.google.chatappstream.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemViewHolderFactory
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private val args: ChatFragmentArgs by navArgs()
    val factory by lazy { MessageListViewModelFactory(args.channelId) }
    val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }
    val messageListViewModel: MessageListViewModel by activityViewModels { factory }
    val messageInputViewModel: MessageInputViewModel by viewModels { factory }

    @Inject
    lateinit var chatClient: ChatClient

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChatBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageListHeaderViewModel.bindView(binding.messageListHeaderView, viewLifecycleOwner)
        messageListViewModel.bindView(binding.messageListView, viewLifecycleOwner)
        messageInputViewModel.bindView(binding.messageInputView, viewLifecycleOwner)

        messageListViewModel.state.observe(viewLifecycleOwner) {
            if (it is MessageListViewModel.State.NavigateUp)
                findNavController().navigateUp()
        }

        binding.messageListHeaderView.setBackButtonClickListener {
            findNavController().navigateUp()
        }

        binding.messageListView.setMessageViewHolderFactory(
            CustomMessageViewHolderFactory(
                myMessageItemClickListener,
                chatClient.getCurrentUser()?.id ?: ""
            )
        )

    }


    private val myMessageItemClickListener = object : MyMessageItemClickListener {
        override fun onMessageClick(msg: MessageListItem.MessageItem) {

        }

        override fun onMessageLongClick(msg: MessageListItem.MessageItem): Boolean {
            val btmsheet = ChatMessageLongClickBtmSheetDialogFragment.getInstance(msg)
            btmsheet.show(childFragmentManager, null)
            return true
        }
    }

    class CustomMessageViewHolderFactory(
        private val listener: MyMessageItemClickListener,
        val userId: String
    ) :
        MessageListItemViewHolderFactory() {
        override fun getItemViewType(item: MessageListItem): Int {
            return if (item is MessageListItem.MessageItem &&
                item.isMine &&
                item.message.attachments.isEmpty() &&
                item.message.createdAt.isLessThenDayAgo()
            ) {
                CHAT_VIEW_HOLDER_RIGHT_TYPE

            } else if (item is MessageListItem.MessageItem &&
                item.isMine &&
                item.message.attachments.isEmpty() &&
                item.message.createdAt.isLessThenDayAgo()
            ) {
                CHAT_VIEW_HOLDER_LEFT_TYPE

            } else super.getItemViewType(item)

        }

        private fun Date?.isLessThenDayAgo(): Boolean {
            if (this == null) {
                return false
            }
            val dayInMillis = TimeUnit.DAYS.toMillis(1)
            return time >= System.currentTimeMillis() - dayInMillis
        }

        override fun createViewHolder(
            parentView: ViewGroup,
            viewType: Int,
        ): BaseMessageItemViewHolder<out MessageListItem> {
            return if (viewType == CHAT_VIEW_HOLDER_RIGHT_TYPE) {
                ChatViewHolderRight(listener, userId, parentView)
            } else {
                ChatViewHolderLeft(listener, userId, parentView)
            }
        }

        companion object {
            private const val CHAT_VIEW_HOLDER_RIGHT_TYPE = 56
            private const val CHAT_VIEW_HOLDER_LEFT_TYPE = 59
        }


    }
}


interface MyMessageItemClickListener {
    fun onMessageClick(msg: MessageListItem.MessageItem)
    fun onMessageLongClick(msg: MessageListItem.MessageItem): Boolean
}