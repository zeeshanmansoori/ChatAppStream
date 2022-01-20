package com.google.chatappstream.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//
//    @Singleton
//    @Provides
//    fun provideChatClient(@ApplicationContext context: Context) =
//        ChatClient.Builder(BuildConfig, context).build()
}