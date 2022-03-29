package com.example.moviedb.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviedb.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM favourites")
    fun getMovies(): PagingSource<Int, Movie>

    @Query("SELECT EXISTS(SELECT * FROM favourites WHERE id = :id)")
    fun isMovieExists(id: Int): Flow<Boolean>
}