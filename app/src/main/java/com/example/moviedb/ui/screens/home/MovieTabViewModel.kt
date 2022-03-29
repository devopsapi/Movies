package com.example.moviedb.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import com.example.moviedb.data.model.Movie
import com.example.moviedb.data.repository.DatabaseRepository
import com.example.moviedb.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
open class MovieTabViewModel @Inject constructor(
    private val networkRepo: NetworkRepository,
    private val databaseRepo: DatabaseRepository,
) : ViewModel() {
    fun getMovies(position: Int): Flow<PagingData<Movie>> {
        return when (position) {
            0 -> networkRepo.getPopularMovies()
            1 -> networkRepo.getNowPlayingMovies()
            2 -> networkRepo.getTopRatedMovies()
            3 -> networkRepo.getUpcomingMovies()
            4 -> databaseRepo.getMovies()
            else -> networkRepo.getPopularMovies()
        }
    }
}


class MovieViewModelFactory @Inject constructor(
    private val networkRepo: NetworkRepository,
    private val databaseRepo: DatabaseRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NetworkRepository::class.java,
            DatabaseRepository::class.java)
            .newInstance(networkRepo, databaseRepo)
    }
}

