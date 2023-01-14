package com.muzamil.rickamortywithpaging3.domain.entity

import com.muzamil.rickamortywithpaging3.GetCharactersQuery


data class CharacterResponse(
    val info: Info?,
    val characters: List<Character>?
)


fun GetCharactersQuery.Data.mapToCharacter() = CharacterResponse(
    info = characters?.info?.let {
        Info(
            pages = it.pages, count = it.count, prev = it.prev, next = it.next
        )
    },
    characters = characters?.results?.let { results ->
        results.map {
            Character(
                id = it?.id!!,
                gender = it?.gender,
                type = it?.type,
                name = it?.name,
                image = it?.image,
                species = it?.species,
                status = it?.status,
                page = characters.info?.pages
            )
        }
    }

)