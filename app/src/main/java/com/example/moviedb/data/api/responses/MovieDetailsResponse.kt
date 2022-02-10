package com.example.moviedb.data.api.responses

class MovieDetailsResponse(
    val title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double?,
    val genres: ArrayList<Genres>,
)

class Genres(val id: Int, val name: String)