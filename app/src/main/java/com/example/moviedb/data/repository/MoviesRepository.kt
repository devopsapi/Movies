package com.example.moviedb.data.repository

import com.example.moviedb.data.api.MovieApi
import com.example.moviedb.data.api.responses.MovieDetailsResponse
import com.example.moviedb.data.api.responses.MoviesResponse
import com.example.moviedb.utils.GeneralErrorHandlerImpl
import com.example.moviedb.utils.toResult
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton
import com.example.moviedb.utils.Result

@Singleton
class MoviesRepository @Inject constructor(var movieApi: MovieApi) {

    fun getPopularMovies(page: Int): Single<Result<MoviesResponse>> {
        return movieApi.getPopularMovies(page)
            .toResult(GeneralErrorHandlerImpl())
            .subscribeOn(Schedulers.io())
    }

    fun getSimilarMovies(movieId: Int, page: Int): Single<Result<MoviesResponse>> {
        return movieApi.getSimilarMovies(movieId, page)
            .toResult(GeneralErrorHandlerImpl())
            .subscribeOn(Schedulers.io())
    }

    fun getMovieDetails(movieId: Int): Single<Result<MovieDetailsResponse>> {
        return movieApi.getMovieDetails(movieId)
            .toResult(GeneralErrorHandlerImpl())
            .subscribeOn(Schedulers.io())
    }
}
