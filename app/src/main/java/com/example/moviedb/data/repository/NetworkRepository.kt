package com.example.moviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviedb.data.api.MovieApi
import com.example.moviedb.data.api.responses.MovieDetailsDTO
import com.example.moviedb.data.model.Movie
import com.example.moviedb.data.paging.MoviesPageLoader
import com.example.moviedb.data.paging.MoviesPagingSource
import com.example.moviedb.utils.GeneralErrorHandlerImpl
import javax.inject.Inject
import javax.inject.Singleton
import com.example.moviedb.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class NetworkRepository @Inject constructor(private val movieApi: MovieApi) {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    fun getPopularMovies(): Flow<PagingData<Movie>> {
        val loader: MoviesPageLoader = { pageIndex ->
            movieApi.getPopularMovies(pageIndex)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(loader) }
        ).flow
    }

    fun getSimilarMovies(movieId: Int): Flow<PagingData<Movie>> {
        val loader: MoviesPageLoader = { pageIndex ->
            movieApi.getSimilarMovies(movieId, pageIndex)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(loader) }
        ).flow
    }

    fun getMovieDetails(movieId: Int): Flow<Result<MovieDetailsDTO>> = flow {
        try {
            val res = movieApi.getMovieDetails(movieId)
            emit(Result.fromData(res))
        } catch (e: Throwable) {
            emit(Result.fromError<MovieDetailsDTO>(GeneralErrorHandlerImpl().getError(e)))
        }
    }

    fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        val loader: MoviesPageLoader = { pageIndex ->
            movieApi.getNowPlayingMovies(pageIndex)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(loader) }
        ).flow
    }

    fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        val loader: MoviesPageLoader = { pageIndex ->
            movieApi.getTopRatedMovies(pageIndex)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(loader) }
        ).flow
    }

    fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        val loader: MoviesPageLoader = { pageIndex ->
            movieApi.getUpcomingMovies(pageIndex)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(loader) }
        ).flow
    }
}