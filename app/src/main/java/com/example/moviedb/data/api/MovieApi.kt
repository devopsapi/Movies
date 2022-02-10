package com.example.moviedb.data.api

import com.example.moviedb.data.api.responses.MovieDetailsResponse
import com.example.moviedb.data.api.responses.MoviesResponse
import com.example.moviedb.data.model.MovieModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int = 1,
    ): Single<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): Single<MovieDetailsResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
    ): Single<MoviesResponse>

    @GET("movie/latest")
    fun getLatestMovies(): Single<MovieModel>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
    ): Single<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("page") page: Int = 1,
    ): Single<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("page") page: Int = 1,
    ): Single<MoviesResponse>

    @GET("search/movie")
    fun searchForMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): Single<MoviesResponse>
}