package com.google.chatappstream.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    fun logout() {
        client.disconnect()
    }

    fun getUser(): User? {
        return client.getCurrentUser()
    }

    suspend fun deleteChannel(channelId: String) {
        val channelClient = client.channel("messaging", channelId)
        val result = channelClient.delete().await()
        if (result.isSuccess)
            Log.d("zeeshan", "deleteChannel: deleted id $channelId")
        else if (result.isError)
            Log.d("zeeshan", "deleteChannel: not deleted id ${result.error()}")

    }

}