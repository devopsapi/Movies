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
class UpcomingMoviesViewModel @Inject constructor(var repo: MoviesRepository) : MovieViewModel() {

    init {
        getData()
    }

    override fun getData() {
        if (!_isLoading.value!! && canLoadMore()) {
            _isLoading.value = true
            compositeDisposable.add(
                repo.getUpcomingMovies(currentPage)
                    .subscribeOn(Schedulers.io())
                    .subscribe { response ->
                        if (response.isSuccess()) {
                            response.data?.movies?.let { allLoadedMovies.addAll(it) }
                            _movieList.postValue(allLoadedMovies)
                            totalPages = response.data?.total_pages ?: 1
                            _isLoading.postValue(false)
                            currentPage++

                            Timber.i("Network request in top rated")
                        } else {
                            _error.value = defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                    })
        }
    }
}