package com.google.chatappstream.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.getstream.sdk.chat.adapter.MessageListItem
import com.google.chatappstream.databinding.ChatListRcvLeftBinding
import com.google.chatappstream.databinding.ChatListRcvRightBinding
import com.google.chatappstream.util.isStar

import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemPayloadDiff

class ChatViewHolderRight(
    private val msgItemClickListener: MyMessageItemClickListener,
    private val userId: String,
    parentView: ViewGroup,
    private val binding: ChatListRcvRightBinding = ChatListRcvRightBinding.inflate(
        LayoutInflater.from(
            parentView.context
        ),
        parentView,
        false
    ),
) : BaseMessageItemViewHolder<MessageListItem.MessageItem>(binding.root) {


    override fun bindData(data: MessageListItem.MessageItem, diff: MessageListItemPayloadDiff?) {
        binding.root.setOnClickListener { msgItemClickListener.onMessageClick(data) }
        binding.root.setOnLongClickListener { msgItemClickListener.onMessageLongClick(data) }
        binding.textLabel.text = data.message.text
        binding.starAction.isChecked = data.isStar(userId)
    }
}

class ChatViewHolderLeft(
    private val msgItemClickListener: MyMessageItemClickListener,
    private val userId: String,
    parentView: ViewGroup,
    private val binding: ChatListRcvLeftBinding = ChatListRcvLeftBinding.inflate(
        LayoutInflater.from(
            parentView.context
        ),
        parentView,
        false
    ),
) : BaseMessageItemViewHolder<MessageListItem.MessageItem>(binding.root) {


    override fun bindData(data: MessageListItem.MessageItem, diff: MessageListItemPayloadDiff?) {
        binding.root.setOnClickListener { msgItemClickListener.onMessageClick(data) }
        binding.root.setOnLongClickListener { msgItemClickListener.onMessageLongClick(data) }
        binding.textLabel.text = data.message.text
        binding.starAction.isChecked = data.isStar(userId)

    }
}