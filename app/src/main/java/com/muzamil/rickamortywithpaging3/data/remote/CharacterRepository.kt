package com.muzamil.rickamortywithpaging3.data.remote

import androidx.paging.PagingData
import com.muzamil.rickamortywithpaging3.GetCharactersQuery
import com.muzamil.rickamortywithpaging3.domain.entity.CharacterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.Response

interface  CharacterRepository {

         fun getCharacterFromDb():Flow<PagingData<com.muzamil.rickamortywithpaging3.domain.entity.Character>>


}