package com.example.moviedb.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.ui.screens.home.tabs.*
import javax.inject.Inject

class MovieViewModelsFactory @Inject constructor(private val repository: MoviesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MoviesRepository::class.java)
            .newInstance(repository)
    }

    fun getMovieViewModel(owner: ViewModelStoreOwner, position: Int): MovieViewModel {
        return when (position) {
            0 -> ViewModelProvider(owner, this)[PopularMoviesListViewModel::class.java]
            1 -> ViewModelProvider(owner, this)[LatestMovieViewModel::class.java]
            2 -> ViewModelProvider(owner, this)[NowPlayingMoviesViewModel::class.java]
            3 -> ViewModelProvider(owner, this)[TopRatedMoviesViewModel::class.java]
            4 -> ViewModelProvider(owner, this)[UpcomingMoviesViewModel::class.java]
            else -> ViewModelProvider(owner, this)[PopularMoviesListViewModel::class.java]
        }
    }
}