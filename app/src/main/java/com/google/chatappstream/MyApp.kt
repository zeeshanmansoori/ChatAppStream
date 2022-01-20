package com.google.chatappstream

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.livedata.ChatDomain
import javax.inject.Inject


@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var chatClient: ChatDomain

}