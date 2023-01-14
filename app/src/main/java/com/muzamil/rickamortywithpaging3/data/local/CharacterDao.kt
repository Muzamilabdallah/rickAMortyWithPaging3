package com.muzamil.rickamortywithpaging3.data.local

import android.graphics.Movie
import androidx.hilt.navigation.compose.R
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muzamil.rickamortywithpaging3.domain.entity.Character
import com.muzamil.rickamortywithpaging3.domain.entity.RemoteKey

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)
    @Query("SELECT * From character ORDER BY page")
      fun getCharacter(): PagingSource<Int, Character>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllRemote(list: List<RemoteKey>)
    @Query("SELECT * From remote_key Where id =:id")
    suspend fun getRemoteKeyById(id: String?): RemoteKey?
    @Query("SELECT created_at From remote_key Order By created_at DESC LIMIT 1")
    suspend fun getLastUpdated(): Long?
    @Query("DELETE From remote_key")
    suspend fun deleteRemoteKeys()
    @Query("DELETE From character")
    suspend fun deleteCharacters()
}