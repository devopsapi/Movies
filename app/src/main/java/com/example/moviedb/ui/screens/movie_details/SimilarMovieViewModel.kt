package com.example.moviedb.ui.screens.movie_details

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
import java.util.ArrayList
import javax.inject.Inject


@HiltViewModel
class SimilarMovieViewModel @Inject constructor(var repo: MoviesRepository) : ViewModel() {

    private val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList: LiveData<List<MovieModel>>
        get() = _movieList
    private val allLoadedMovies = ArrayList<MovieModel>()

    private var totalPages: Int = 1
    private var currentPage: Int = 1
    private val compositeDisposable = CompositeDisposable()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _noSimilarMovies = MutableLiveData("")
    val noSimilarMovies: LiveData<String>
        get() = _noSimilarMovies

    fun getSimilarMovies(movieId: Int) {
        if (!_isLoading.value!! && canLoadMore()) {
            _isLoading.value = true
            compositeDisposable.add(
                repo.getSimilarMovies(movieId, currentPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { response ->
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
                            _error.value = defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                    }
            )
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