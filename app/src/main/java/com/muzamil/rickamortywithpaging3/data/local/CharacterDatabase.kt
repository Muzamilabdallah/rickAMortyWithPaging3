package com.muzamil.rickamortywithpaging3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muzamil.rickamortywithpaging3.domain.entity.Character
import com.muzamil.rickamortywithpaging3.domain.entity.RemoteKey

@Database(
    entities = [Character::class,RemoteKey::class],
    version = 1,

    )
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}