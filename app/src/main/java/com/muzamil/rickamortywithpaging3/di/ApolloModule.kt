package com.muzamil.rickamortywithpaging3.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.muzamil.rickamortywithpaging3.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    @Singleton
    @Provides
    fun provideNetwork() = getApolloClient()
    private fun getApolloClient(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.Builder()
            .serverUrl(Constant.BASE_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

}