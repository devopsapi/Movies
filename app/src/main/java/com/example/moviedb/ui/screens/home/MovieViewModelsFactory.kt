package com.example.moviedb.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.moviedb.data.repository.DatabaseRepository
import com.example.moviedb.data.repository.NetworkRepository
import com.example.moviedb.ui.screens.home.tabs.*
import javax.inject.Inject

class MovieViewModelsFactory @Inject constructor(
    val networkViewModelsFactory: NetworkViewModelsFactory,
    val databaseViewModelsFactory: DatabaseViewModelsFactory,
) {
    fun getMovieViewModel(owner: ViewModelStoreOwner, position: Int): MovieViewModel {
        return when (position) {
            0 -> ViewModelProvider(owner,
                networkViewModelsFactory)[PopularMoviesListViewModel::class.java]
            1 -> ViewModelProvider(owner,
                networkViewModelsFactory)[LatestMovieViewModel::class.java]
            2 -> ViewModelProvider(owner,
                networkViewModelsFactory)[NowPlayingMoviesViewModel::class.java]
            3 -> ViewModelProvider(owner,
                networkViewModelsFactory)[TopRatedMoviesViewModel::class.java]
            4 -> ViewModelProvider(owner,
                networkViewModelsFactory)[UpcomingMoviesViewModel::class.java]
            5 -> ViewModelProvider(owner,
                databaseViewModelsFactory)[FavouriteMoviesViewModel::class.java]
            else -> ViewModelProvider(owner,
                networkViewModelsFactory)[PopularMoviesListViewModel::class.java]
        }
    }
}


class NetworkViewModelsFactory @Inject constructor(private val repository: NetworkRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NetworkRepository::class.java)
            .newInstance(repository)
    }
}

class DatabaseViewModelsFactory @Inject constructor(private val repository: DatabaseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DatabaseRepository::class.java)
            .newInstance(repository)
    }
}