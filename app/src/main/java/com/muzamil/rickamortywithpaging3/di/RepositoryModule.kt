package com.muzamil.rickamortywithpaging3.di

import com.apollographql.apollo3.ApolloClient
import com.muzamil.rickamortywithpaging3.data.local.CharacterDao
import com.muzamil.rickamortywithpaging3.data.remote.CharacterRepository
import com.muzamil.rickamortywithpaging3.data.remote.CharacterRepositoryImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideChatRepository(
        apolloClient: ApolloClient,
        characterDao: CharacterDao
    ): CharacterRepository =
        CharacterRepositoryImpl(characterDao = characterDao, apolloClient = apolloClient)
}