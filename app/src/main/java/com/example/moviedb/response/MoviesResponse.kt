package com.example.moviedb.response

import com.example.moviedb.model.MovieModel
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val popularMovies: List<MovieModel>
)