package com.example.moviedb.ui.screens.home.tabs

import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.ui.screens.home.MovieViewModel
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LatestMovieViewModel @Inject constructor(var repo: MoviesRepository) : MovieViewModel() {

    init {
        getLatestMovies()
    }

    fun getLatestMovies() {
        if (!isLoading.value!!) {
            _isLoading.value = true
            compositeDisposable.add(
                repo.getLatestMovie()
                    .subscribeOn(Schedulers.io())
                    .subscribe { response ->
                        if (response.isSuccess()) {
                            response.data?.let { allLoadedMovies.add(it) }
                            _movieList.postValue(allLoadedMovies)
                            _isLoading.postValue(false)
                            Timber.i("Network request in latest")
                        } else {
                            _error.value = defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                    }
            )
        }
    }
}