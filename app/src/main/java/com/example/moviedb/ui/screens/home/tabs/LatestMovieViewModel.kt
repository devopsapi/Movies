package com.example.moviedb.ui.screens.home.tabs

import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LatestMovieViewModel @Inject constructor(var repo: MoviesRepository) : MovieViewModel() {

    init {
        Timber.i("latest viewModel created")
        getData()
    }

    override fun getData() {
        if (!isLoading.value!!) {
            viewModelScope.launch {
                _isLoading.postValue(true)
                repo.getLatestMovie()
                    .collect { response ->
                        if (response.isSuccess()) {
                            response.data?.let { allLoadedMovies.add(it) }
                            _movieList.postValue(allLoadedMovies)
                            _isLoading.postValue(false)
                            Timber.i("Network request in latest")
                        } else {
                            _error.value =
                                defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                    }
            }
        }
    }
}