package com.google.chatappstream.util

import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.client.models.Message

val Message.userList: List<String>
    get() = try {
        extraData.get(Constants.STAR_BY_USERS) as List<String>
    } catch (e: Exception) {
        emptyList()
    }


fun MessageListItem.MessageItem.isStar(userId: String) = message.userList.contains(userId)
