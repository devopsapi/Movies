package com.example.moviedb.ui.screens.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.api.responses.MovieDetailsResponse
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(var repo: MoviesRepository) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetailsResponse>()
    val movieDetails: LiveData<MovieDetailsResponse>
        get() = _movieDetails

    private val compositeDisposable = CompositeDisposable()


    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getMovieDetails(movieId: Int) {
        compositeDisposable.add(
            repo.getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    if (response.isSuccess()) {
                        _movieDetails.value = response.data
                    } else {
                        _error.value = defineErrorType(response.error ?: ErrorEntity.Unknown)
                    }
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}