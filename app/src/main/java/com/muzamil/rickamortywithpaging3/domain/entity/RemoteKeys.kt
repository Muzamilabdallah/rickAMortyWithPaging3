package com.muzamil.rickamortywithpaging3.domain.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKey(
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val characterId: String,
    val page: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    val created_at: Long = System.currentTimeMillis()

)