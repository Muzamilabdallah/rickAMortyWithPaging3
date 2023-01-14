package com.muzamil.rickamortywithpaging3.data.remote

import android.nfc.tech.MifareUltralight
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.muzamil.rickamortywithpaging3.GetCharactersQuery
import com.muzamil.rickamortywithpaging3.data.local.CharacterDao
import com.muzamil.rickamortywithpaging3.domain.entity.Character
import com.muzamil.rickamortywithpaging3.domain.entity.CharacterResponse
import com.muzamil.rickamortywithpaging3.domain.entity.mapToCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val characterDao: CharacterDao
) : CharacterRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacterFromDb(): Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = 10,
                initialLoadSize = MifareUltralight.PAGE_SIZE,
            ),
            pagingSourceFactory = {
                characterDao.getCharacter()
            },
            remoteMediator = CharacterRemoteMediator(
                apolloClient,
                characterDao,
            )
        ).flow

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

}