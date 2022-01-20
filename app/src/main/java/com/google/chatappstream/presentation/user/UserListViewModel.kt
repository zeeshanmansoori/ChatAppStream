package com.google.chatappstream.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.call.await
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.models.name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {


    private var _userListState = MutableSharedFlow<UserListEvent>()
    val userListState get() = _userListState

    private val _users = MutableSharedFlow<List<User>?>()
    val users get() = _users

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllUsers()
        }

    }


    suspend fun createNewChannel(
        selectedUser: User
    ) {
        val result = client.createChannel(
            channelType = "messaging",
            members = listOf(client.getCurrentUser()!!.id, selectedUser.id),
            extraData = mapOf("channelName" to selectedUser.name)
        ).await()

        if (result.isError)
            _userListState.emit(UserListEvent.Error(result.error().message))
        if (result.isSuccess)
            _userListState.emit(UserListEvent.ChannelCreated(result.data().cid))
    }


    sealed class UserListEvent() {
        data class Error(val error: String?) : UserListEvent()
        data class ChannelCreated(val channelId: String) : UserListEvent()
    }

    private suspend fun getAllUsers() {
        val requester = QueryUsersRequest(
            filter = Filters.ne("id", client.getCurrentUser()!!.id),
            offset = 0,
            limit = 50
        )

        val result = client.queryUsers(requester).await()
        if (result.isSuccess)
            _users.emit(result.data())
        else _users.emit(null)

    }
}