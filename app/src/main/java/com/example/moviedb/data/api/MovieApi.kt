package com.example.moviedb.data.api

import com.example.moviedb.data.api.responses.MovieDetailsResponse
import com.example.moviedb.data.api.responses.MoviesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKet: String,
        @Query("page") page: Int = 1
    ): Single<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKet: String
    ): Single<MovieDetailsResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Single<MoviesResponse>
}