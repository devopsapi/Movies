package com.example.moviedb.data.repository

import com.example.moviedb.data.db.MovieDao
import com.example.moviedb.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(val movieDao: MovieDao) {

    fun getMovies(): Flow<List<Movie>> {
        return movieDao.getMovies()
    }


    suspend fun saveMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

    fun isExist(movieId: Int): Flow<Boolean> {
        return movieDao.isMovieExists(movieId)
    }

//    fun isExist(movieId: Int): Flow<Boolean> = flow {
//        emit(movieDao.isMovieExists(movieId))
//    }.flowOn(Dispatchers.IO)
}