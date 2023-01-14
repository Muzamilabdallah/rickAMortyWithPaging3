package com.muzamil.rickamortywithpaging3.data.remote


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.muzamil.rickamortywithpaging3.GetCharactersQuery
import com.muzamil.rickamortywithpaging3.data.local.CharacterDatabase
import com.muzamil.rickamortywithpaging3.domain.entity.Character
import com.muzamil.rickamortywithpaging3.domain.entity.RemoteKey
import com.muzamil.rickamortywithpaging3.domain.entity.mapToCharacter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val apolloClient: ApolloClient,
    private val db: CharacterDatabase
) : RemoteMediator<Int, Character>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES)
        val lastUpdated = db.characterDao().getLastUpdated() ?: 0

        return if (System.currentTimeMillis() - (lastUpdated) < cacheTimeout) {
            // Cached data is up-to-date, so there is no need to re- fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val result =
                apolloClient.query(GetCharactersQuery(page = page)).execute()
            val endOfPaginationReached = result.data?.characters?.results?.isEmpty()
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached == true) null else page + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.characterDao().deleteCharacters()
                    db.characterDao().deleteRemoteKeys()
                }

                val remoteKeys = result?.data?.characters?.results?.map {
                    RemoteKey(
                        characterId = it?.id!!,
                        prevKey = prevKey,
                        page = page,
                        nextKey = nextKey
                    )
                }

                remoteKeys?.let {
                    db.characterDao().insertAllRemote(
                        it
                    )
                }


            result?.data?.mapToCharacter()?.characters?.let {
                db.characterDao().insertAll( it.onEachIndexed { _, character -> character.page = page })
            }

        }
        return RemoteMediator.MediatorResult.Success(endOfPaginationReached = endOfPaginationReached == true)

    } catch (e: ApolloException)
    {
        return RemoteMediator.MediatorResult.Error(e)
    }
}

private suspend fun getKeyPageData(
    loadType: LoadType,
    state: PagingState<Int, com.muzamil.rickamortywithpaging3.domain.entity.Character>
): Any {
    return when (loadType) {
        LoadType.REFRESH -> {
            val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
            remoteKeys?.nextKey?.minus(1) ?: 1
        }
        LoadType.APPEND -> {

            val remoteKeys = getRemoteKeyForLastItem(state)
            val nextKey = remoteKeys?.nextKey
            return nextKey ?: RemoteMediator.MediatorResult.Success(endOfPaginationReached = false)
        }
        LoadType.PREPEND -> {

            val remoteKeys = getRemoteKeyForFirstItem(state)
            val prevKey = remoteKeys?.prevKey ?: return RemoteMediator.MediatorResult.Success(
                endOfPaginationReached = false
            )
            prevKey
        }
    }
}


private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Character>): RemoteKey? {
    return state.anchorPosition?.let { position ->
        state.closestItemToPosition(position)?.id?.let { id ->
            db.characterDao().getRemoteKeyById(id)
        }
    }
}

private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Character>): RemoteKey? {
    return state.pages.firstOrNull {
        it.data.isNotEmpty()
    }?.data?.firstOrNull()?.let { character ->
        db.characterDao().getRemoteKeyById(character.id)
    }
}

private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Character>): RemoteKey? {
    return state.pages.lastOrNull {
        it.data.isNotEmpty()
    }?.data?.lastOrNull()?.let { character ->
        db.characterDao().getRemoteKeyById(character.id)

    }
}


}