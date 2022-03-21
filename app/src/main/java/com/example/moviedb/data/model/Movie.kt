package com.example.moviedb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Movie(
    @PrimaryKey val id: Int,
    val poster_path: String,
)