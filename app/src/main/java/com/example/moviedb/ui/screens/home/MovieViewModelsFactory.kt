package com.example.moviedb.ui.screens.home

import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.ui.screens.home.tabs.LatestMovieViewModel
import com.example.moviedb.ui.screens.home.tabs.NowPlayingMoviesViewModel
import com.example.moviedb.ui.screens.home.tabs.PopularMoviesListViewModel
import com.example.moviedb.ui.screens.home.tabs.TopRatedMoviesViewModel
import com.example.moviedb.ui.screens.home.tabs.UpcomingMoviesViewModel
import javax.inject.Inject

class MovieViewModelsFactory @Inject constructor(val repo: MoviesRepository) {

    fun getMovieViewModel(position: Int): MovieViewModel {
        return when (position) {
            0 -> PopularMoviesListViewModel(repo)
            1 -> LatestMovieViewModel(repo)
            2 -> NowPlayingMoviesViewModel(repo)
            3 -> TopRatedMoviesViewModel(repo)
            4 -> UpcomingMoviesViewModel(repo)
            else -> PopularMoviesListViewModel(repo)
        }
    }
}