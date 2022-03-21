package com.example.moviedb.data.db

import androidx.room.*
import com.example.moviedb.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Delete()
    suspend fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM favourites")
    fun getMovies(): Flow<List<Movie>>

    @Query("SELECT EXISTS(SELECT * FROM favourites WHERE id = :id)")
    fun isMovieExists(id: Int): Flow<Boolean>

//    @Query("SELECT * FROM favourites")
//    fun getMovies(): List<Movie>
//
//    @Query("SELECT EXISTS(SELECT * FROM favourites WHERE id = :id)")
//    fun isMovieExists(id: Int): Boolean
}