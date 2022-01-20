package com.example.moviedb.data.api.responses

import com.example.moviedb.data.model.MovieModel
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val movies: List<MovieModel>
)