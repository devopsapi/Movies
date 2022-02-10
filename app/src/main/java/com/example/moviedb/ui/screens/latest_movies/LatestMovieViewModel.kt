package com.example.moviedb.ui.screens.latest_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LatestMovieViewModel @Inject constructor(var repo: MoviesRepository) : ViewModel() {

    private val _latestMovieList = MutableLiveData<MovieModel>()
    val latestMovieList: LiveData<MovieModel>
        get() = _latestMovieList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        getLatestMovies()
    }

    fun getLatestMovies() {
        if (!isLoading.value!!) {
            _isLoading.value = true
            compositeDisposable.add(
                repo.getLatestMovie()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { response ->
                        if (response.isSuccess()) {
                            _latestMovieList.value = response.data
                            _isLoading.value = false
                            Timber.i("Network request in latest")
                        } else {
                            _error.value = defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                    }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}