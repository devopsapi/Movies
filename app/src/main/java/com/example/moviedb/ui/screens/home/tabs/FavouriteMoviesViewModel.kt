package com.example.moviedb.ui.screens.home.tabs

import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavouriteMoviesViewModel @Inject constructor(val databaseRepo: DatabaseRepository) :
    MovieViewModel() {

    init {
        Timber.i("favourites viewModel created")
        getData()
    }

    override fun getData() {
        if (!_isLoading.value!! && canLoadMore()) {
            viewModelScope.launch {
                _isLoading.postValue(true)
                databaseRepo
                    .getMovies()
                    .collect { response ->
                        _movieList.postValue(response)
                        _isLoading.postValue(false)
                        Timber.i("TOTAL: ${response.size}")
                        Timber.i("Database request in favourites")
                    }
            }
        }
    }
}