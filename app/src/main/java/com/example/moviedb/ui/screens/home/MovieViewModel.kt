package com.example.moviedb.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
open class MovieViewModel @Inject constructor(): ViewModel() {

    protected val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList: LiveData<List<MovieModel>>
        get() = _movieList
    protected val allLoadedMovies = ArrayList<MovieModel>()

    protected var currentPage = 1
    protected var totalPages: Int = 1
    protected val compositeDisposable = CompositeDisposable()

    protected val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    protected fun canLoadMore(): Boolean {
        if (currentPage == 1) {
            return true
        }
        return currentPage < totalPages
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

     open fun getData(){
         return
     }
}

