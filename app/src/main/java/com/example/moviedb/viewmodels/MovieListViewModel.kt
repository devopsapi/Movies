package com.example.moviedb.viewmodels

import android.util.Log
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
class MovieListViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repo: MoviesRepository
    private val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList: LiveData<List<MovieModel>>
        get() = _movieList

    private var currentPage = 1
    private var totalPages: Int = 1
    private val compositeDisposable = CompositeDisposable()

    fun getPopularMovies() {
        compositeDisposable.add(
            repo.getPopularMovies(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    _movieList.value = response.movies
                    totalPages = response.total_pages

                    Log.i("TAG", "NETWORK REQUEST")
                }
        )
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