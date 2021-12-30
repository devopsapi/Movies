package com.example.moviedb.response

import com.example.moviedb.model.MovieModel
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("total_results") val total_movies: Int,
    @SerializedName("results") val popularMovies: List<MovieModel>
)