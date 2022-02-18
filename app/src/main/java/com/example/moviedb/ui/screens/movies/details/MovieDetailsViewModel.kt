package com.example.moviedb.ui.screens.movies.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.api.responses.MovieDetails
import com.example.moviedb.data.api.responses.convertToMovieDetails
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(var repo: MoviesRepository) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails>
        get() = _movieDetails

    private val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList: LiveData<List<MovieModel>>
        get() = _movieList
    private val allLoadedMovies = ArrayList<MovieModel>()

    private var totalPages: Int = 1
    private var currentPage: Int = 1

    private val _noSimilarMovies = MutableLiveData("")
    val noSimilarMovies: LiveData<String>
        get() = _noSimilarMovies

    private val _movieDetailsError = MutableLiveData<String>()
    val movieDetailsError: LiveData<String>
        get() = _movieDetailsError

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    private val compositeDisposable = CompositeDisposable()

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            repo.getMovieDetails(movieId)
                .collect { response ->
                    if (response.isSuccess()) {
                        _movieDetails.value = response.data?.convertToMovieDetails()

                        Timber.i("Network request in details")
                    } else {
                        _movieDetailsError.value =
                            defineErrorType(response.error ?: ErrorEntity.Unknown)
                        _movieDetailsError.value = response.error.toString()
                    }
                }
        }
        _isLoading.value = false
    }


    fun getSimilarMovies(movieId: Int) {
        if (!_isLoading.value!! && canLoadMore()) {
            viewModelScope.launch {
                _isLoading.value = true
                repo.getSimilarMovies(movieId, currentPage)
                    .collect { response ->
                        if (response.isSuccess()) {
                            if (response.data?.movies?.isEmpty() == true) {
                                _noSimilarMovies.value = "No similar movies"
                            } else {
                                response.data?.movies?.let { allLoadedMovies.addAll(it) }
                                _movieList.value = allLoadedMovies
                                totalPages = response.data?.total_pages ?: 1
                                currentPage++
                            }
                            _isLoading.value = false

                            Timber.i("Network request in similar")
                        } else {
                            _movieDetailsError.value =
                                defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                    }
            }
        }
    }

    private fun canLoadMore(): Boolean {
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
}