package com.example.moviedb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class SimilarMovieViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repo: MoviesRepository
    private val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList: LiveData<List<MovieModel>>
        get() = _movieList

    private var totalPages: Int = 1
    private var currentPage: Int = 1
    private var movieId: Int = 0
    private val compositeDisposable = CompositeDisposable()

    fun getSimilarMovies() {
        compositeDisposable.add(
            repo.getSimilarMovies(movieId, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    _movieList.value = response.movies
                    totalPages = response.total_pages
                }
        )
    }

    fun setMovieId(id: Int) {
        movieId = id
    }

    fun updatePage() {
        currentPage++
    }

    fun canLoadMore(): Boolean {
        return currentPage < totalPages
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}