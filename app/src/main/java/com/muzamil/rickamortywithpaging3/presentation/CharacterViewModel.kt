package com.muzamil.rickamortywithpaging3.presentation

import android.graphics.Movie
import android.nfc.tech.MifareUltralight
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.apollographql.apollo3.ApolloClient
import com.muzamil.rickamortywithpaging3.data.local.CharacterDao
import com.muzamil.rickamortywithpaging3.data.remote.CharacterRemoteMediator
import com.muzamil.rickamortywithpaging3.data.remote.CharacterRepository
import com.muzamil.rickamortywithpaging3.domain.entity.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) :
    ViewModel() {

    val pager: Flow<PagingData<Character>> =
        characterRepository.getCharacterFromDb().cachedIn(viewModelScope)

}