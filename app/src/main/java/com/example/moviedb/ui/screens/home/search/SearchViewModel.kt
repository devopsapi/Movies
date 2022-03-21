package com.example.moviedb.ui.screens.home.search

import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.repository.NetworkRepository
import com.example.moviedb.ui.screens.home.tabs.MovieViewModel
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val repo: NetworkRepository) : MovieViewModel() {

    fun searchForMovie(query: String) {
        if (!_isLoading.value!! && canLoadMore())
            viewModelScope.launch {
                _isLoading.postValue(true)
                repo.searchForMovie(query, currentPage)
                    .collect { response ->
                        if (response.isSuccess()) {
                            totalPages = response.data?.total_pages ?: 1
                            response.data?.movies?.let { allLoadedMovies.addAll(it) }
                            _movieList.postValue(allLoadedMovies)
                            currentPage++
                            _isLoading.postValue(false)
                            Timber.i("Network request in search ")
                        } else {
                            _responseMessage.value = defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                    }
            }
    }
}