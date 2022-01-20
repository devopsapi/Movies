package com.example.moviedb.data.api.responses

class MovieDetailsResponse(
    val title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val vote_count: Int
)