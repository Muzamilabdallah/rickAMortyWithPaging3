package com.muzamil.rickamortywithpaging3.domain.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "character")
data class Character(

    @PrimaryKey(autoGenerate = false)
    val id: String,
    val gender: String?,
    val image: String?,
    val name: String?,
    val species: String?,
    val status: String?,
    val type: String?,
    @ColumnInfo(name = "page")
    var page: Int?,
)