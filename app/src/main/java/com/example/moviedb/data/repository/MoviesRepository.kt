package com.example.moviedb.data.repository

import com.example.moviedb.data.api.MovieApi
import com.example.moviedb.data.api.responses.MovieDetailsDTO
import com.example.moviedb.data.api.responses.MoviesResponse
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.utils.GeneralErrorHandlerImpl
import javax.inject.Inject
import javax.inject.Singleton
import com.example.moviedb.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class MoviesRepository @Inject constructor(var movieApi: MovieApi) {

    fun getPopularMovies(page: Int): Flow<Result<MoviesResponse>> = flow {
        try {
            emit(Result.fromData(movieApi.getPopularMovies(page)))
        } catch (e: Throwable) {
            emit(Result.fromError<MoviesResponse>(GeneralErrorHandlerImpl().getError(e)))
        }
    }

    fun getSimilarMovies(movieId: Int, page: Int): Flow<Result<out MoviesResponse>> = flow {
        try {
            emit(Result.fromData(movieApi.getSimilarMovies(movieId, page)))
        } catch (e: Throwable) {
            emit(Result.fromError<MoviesResponse>(GeneralErrorHandlerImpl().getError(e)))
        }
    }

    fun getMovieDetails(movieId: Int): Flow<Result<MovieDetailsDTO>> = flow {
        try {
            val res = movieApi.getMovieDetails(movieId)
            emit(Result.fromData(res))
        } catch (e: Throwable) {
            emit(Result.fromError<MovieDetailsDTO>(GeneralErrorHandlerImpl().getError(e)))
        }
    }

    fun getLatestMovie(): Flow<Result<MovieModel>> = flow {
        try {
            emit(Result.fromData(movieApi.getLatestMovies()))
        } catch (e: Throwable) {
            emit(Result.fromError<MovieModel>(GeneralErrorHandlerImpl().getError(e)))
        }
    }


    fun getNowPlayingMovies(page: Int): Flow<Result<MoviesResponse>> = flow {
        try {
            emit(Result.fromData(movieApi.getNowPlayingMovies(page)))
        } catch (e: Throwable) {
            emit(Result.fromError<MoviesResponse>(GeneralErrorHandlerImpl().getError(e)))
        }
    }

    fun getTopRatedMovies(page: Int): Flow<Result<MoviesResponse>> = flow {
        try {
            emit(Result.fromData(movieApi.getTopRatedMovies(page)))
        } catch (e: Throwable) {
            emit(Result.fromError<MoviesResponse>(GeneralErrorHandlerImpl().getError(e)))
        }
    }

    fun getUpcomingMovies(page: Int): Flow<Result<MoviesResponse>> = flow {
        try {
            emit(Result.fromData(movieApi.getUpcomingMovies(page)))
        } catch (e: Throwable) {
            emit(Result.fromError<MoviesResponse>(GeneralErrorHandlerImpl().getError(e)))
        }
    }

    fun searchForMovie(query: String, page: Int): Flow<Result<MoviesResponse>> = flow {
        try {
            emit(Result.fromData(movieApi.searchForMovie(query, page)))
        } catch (e: Throwable) {
            emit(Result.fromError<MoviesResponse>(GeneralErrorHandlerImpl().getError(e)))
        }
    }
}