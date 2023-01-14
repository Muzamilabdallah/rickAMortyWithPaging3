package com.muzamil.rickamortywithpaging3.di

import android.content.Context
import androidx.room.Room
import com.muzamil.rickamortywithpaging3.data.local.CharacterDao
import com.muzamil.rickamortywithpaging3.data.local.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton

    fun provideCharacterDb(@ApplicationContext appContext: Context): CharacterDatabase {
        return Room.databaseBuilder(
            appContext,
            CharacterDatabase::class.java,
            "characterDb"
        ).build()
    }


    @Singleton
    @Provides
    fun provideCharacterDao(database: CharacterDatabase): CharacterDao {
        return database.characterDao()
    }

}
