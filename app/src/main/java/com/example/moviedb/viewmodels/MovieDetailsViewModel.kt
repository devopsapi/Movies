package com.example.moviedb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.api.responses.MovieDetailsResponse
import com.example.moviedb.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repo: MoviesRepository
    private val _movieDetails = MutableLiveData<MovieDetailsResponse>()
    val movieDetails: LiveData<MovieDetailsResponse>
        get() = _movieDetails

    private var movieId: Int = 0
    private val compositeDisposable = CompositeDisposable()


    fun getMovieDetails() {
        compositeDisposable.add(
            repo.getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> _movieDetails.value = response }
        )
    }

    fun setMovieId(id: Int) {
        movieId = id
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}