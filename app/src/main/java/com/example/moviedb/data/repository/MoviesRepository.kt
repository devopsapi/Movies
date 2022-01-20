package com.example.moviedb.data.repository

import com.example.moviedb.data.api.MovieApi
import com.example.moviedb.data.api.responses.MovieDetailsResponse
import com.example.moviedb.data.api.responses.MoviesResponse
import com.example.moviedb.utils.Credentials
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor() {

    @Inject
    lateinit var movieApi: MovieApi

    fun getPopularMovies(page: Int): Single<MoviesResponse> =
        movieApi.getPopularMovies(Credentials.API_KEY, page)

    fun getSimilarMovies(movieId: Int, page: Int): Single<MoviesResponse> =
        movieApi.getSimilarMovies(movieId, Credentials.API_KEY, page)

    fun getMovieDetails(movieId: Int): Single<MovieDetailsResponse> =
        movieApi.getMovieDetails(movieId, Credentials.API_KEY)

}