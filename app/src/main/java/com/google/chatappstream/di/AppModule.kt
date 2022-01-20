package com.google.chatappstream.di

import android.content.Context
import com.google.chatappstream.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.livedata.ChatDomain
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideChatClient(@ApplicationContext context: Context) =
        ChatClient.Builder(BuildConfig.apiKey, context).build()

    @Singleton
    @Provides
    fun provideChatDomain(
        chatClient: ChatClient,
        @ApplicationContext context: Context
    ): ChatDomain =ChatDomain.Builder(chatClient, context).build()
}