package com.example.moviedb.ui.screens.home.tabs

import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.repository.NetworkRepository
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(var repo: NetworkRepository) : MovieViewModel() {

    init {
        Timber.i("upcoming viewModel created")
        getData()
    }

    override fun getData() {
        if (!_isLoading.value!! && canLoadMore()) {
            viewModelScope.launch {
                _isLoading.postValue(true)
                repo.getUpcomingMovies(currentPage)
                    .collect { response ->
                        if (response.isSuccess()) {
                            response.data?.movies?.let { allLoadedMovies.addAll(it) }
                            _movieList.postValue(allLoadedMovies)
                            totalPages = response.data?.total_pages ?: 1
                            _isLoading.postValue(false)
                            currentPage++

                            Timber.i("Network request in upcoming")
                        } else {
                            _responseMessage.value = defineErrorType(response.error)
                        }
                    }
            }
        }
    }
}