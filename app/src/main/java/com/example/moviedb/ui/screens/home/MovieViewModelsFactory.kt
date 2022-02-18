package com.example.moviedb.ui.screens.home

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.ui.screens.home.tabs.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieViewModelsFactory @Inject constructor(val repo: MoviesRepository) {

    fun getMovieViewModel(owner: ViewModelStoreOwner,position: Int): MovieViewModel {
        return when (position) {
            0 -> ViewModelProvider(owner)[PopularMoviesListViewModel(repo)::class.java]
            1 -> ViewModelProvider(owner)[LatestMovieViewModel(repo)::class.java]
            2 -> ViewModelProvider(owner)[NowPlayingMoviesViewModel(repo)::class.java]
            3 -> ViewModelProvider(owner)[TopRatedMoviesViewModel(repo)::class.java]
            4 -> ViewModelProvider(owner)[UpcomingMoviesViewModel(repo)::class.java]
            else -> ViewModelProvider(owner)[PopularMoviesListViewModel(repo)::class.java]
        }
    }
}