package com.example.moviedb.ui.screens.search_movie

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
class SearchViewModel @Inject constructor(val repo: MoviesRepository) : ViewModel() {

    private val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList: LiveData<List<MovieModel>>
        get() = _movieList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val allMoviesList = ArrayList<MovieModel>()

    private var totalPages = 1
    private var currentPage = 1

    private val compositeDisposable = CompositeDisposable()

    fun searchForMovie(query: String) {
        if (!_isLoading.value!! && canLadMore())
            _isLoading.value = true
        compositeDisposable.add(repo.searchForMovie(query, currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                if (response.isSuccess()) {
                    totalPages = response.data?.total_pages ?: 1
                    response.data?.movies?.let { allMoviesList.addAll(it) }
                    _movieList.value = allMoviesList
                    currentPage++
                    _isLoading.value = false
                    Timber.i("Network request in search ")
                } else {
                    _error.value = defineErrorType(response.error ?: ErrorEntity.Unknown)
                }
            })
    }

    private fun canLadMore(): Boolean {
        if (currentPage == 1) return true
        return currentPage < totalPages
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}