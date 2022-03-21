package com.example.moviedb.ui.screens.home.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
open class MovieViewModel @Inject constructor() : ViewModel() {

    protected val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList
    protected val allLoadedMovies = ArrayList<Movie>()

    protected var currentPage = 1
    protected var totalPages: Int = 1

    protected val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String>
        get() = _responseMessage

    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    protected fun canLoadMore(): Boolean {
        if (currentPage == 1) {
            return true
        }
        return currentPage < totalPages
    }

    open fun getData() {
        return
    }
}

