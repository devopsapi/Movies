package com.example.moviedb.data.repository

import com.example.moviedb.data.api.MovieApi
import com.example.moviedb.data.api.responses.MovieDetailsDTO
import com.example.moviedb.data.api.responses.MoviesResponse
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.utils.GeneralErrorHandlerImpl
import com.example.moviedb.utils.toResult
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton
import com.example.moviedb.utils.Result

@Singleton
class MoviesRepository @Inject constructor(var movieApi: MovieApi) {

    fun getPopularMovies(page: Int): Single<Result<MoviesResponse>> {
        return movieApi.getPopularMovies(page)
            .toResult(GeneralErrorHandlerImpl())
    }

    fun getSimilarMovies(movieId: Int, page: Int): Single<Result<MoviesResponse>> {
        return movieApi.getSimilarMovies(movieId, page)
            .toResult(GeneralErrorHandlerImpl())
    }

    fun getMovieDetails(movieId: Int): Single<Result<MovieDetailsDTO>> {
        return movieApi.getMovieDetails(movieId)
            .toResult(GeneralErrorHandlerImpl())
    }

    fun getLatestMovie(): Single<Result<MovieModel>> {
        return movieApi.getLatestMovies()
            .toResult(GeneralErrorHandlerImpl())
    }


    fun getNowPlayingMovies(page: Int): Single<Result<MoviesResponse>> {
        return movieApi.getNowPlayingMovies(page)
            .toResult(GeneralErrorHandlerImpl())
    }

    fun getTopRatedMovies(page: Int): Single<Result<MoviesResponse>> {
        return movieApi.getTopRatedMovies(page)
            .toResult(GeneralErrorHandlerImpl())
    }

    fun getUpcomingMovies(page: Int): Single<Result<MoviesResponse>> {
        return movieApi.getUpcomingMovies(page)
            .toResult(GeneralErrorHandlerImpl())
    }

    fun searchForMovie(query: String, page: Int): Single<Result<MoviesResponse>> {
        return movieApi.searchForMovie(query, page)
            .toResult(GeneralErrorHandlerImpl())
    }
}
