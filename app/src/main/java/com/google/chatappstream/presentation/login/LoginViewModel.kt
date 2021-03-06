package com.google.chatappstream.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LogInEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private fun isValidUsername(username: String) =
        username.length >= 4


    fun currentUser(): User? {
        return client.getCurrentUser()
    }

    fun connectUser(username: String) {
        val trimmedUsername = username.trim()
        viewModelScope.launch {
            if (isValidUsername(trimmedUsername)) {
                val result = client.connectUser(
                    user = User(id = trimmedUsername),
                    token = client.devToken(trimmedUsername)
                ).await()
                if (result.isError) {
                    _loginEvent.emit(
                        LogInEvent.ErrorLogIn(
                            result.error().message ?: "Unknown error"
                        )
                    )
                    return@launch
                }
                _loginEvent.emit(LogInEvent.Success)
            } else {
                _loginEvent.emit(LogInEvent.ErrorInputTooShort)
            }
        }
    }

    sealed class LogInEvent {
        object ErrorInputTooShort : LogInEvent()
        data class ErrorLogIn(val error: String) : LogInEvent()
        object Success : LogInEvent()
    }
}