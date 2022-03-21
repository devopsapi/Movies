package com.example.moviedb.ui.screens.moviedetails

import androidx.lifecycle.*
import com.example.moviedb.data.api.responses.MovieDetails
import com.example.moviedb.data.api.responses.convertToMovieDetails
import com.example.moviedb.data.model.Movie
import com.example.moviedb.data.repository.DatabaseRepository
import com.example.moviedb.data.repository.NetworkRepository
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    val networkRepo: NetworkRepository,
    val databaseRepo: DatabaseRepository,
) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails>
        get() = _movieDetails

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList
    private val allLoadedMovies = ArrayList<Movie>()

    private var totalPages: Int = 1
    private var currentPage: Int = 1

    private val _noSimilarMovies = MutableLiveData("")
    val noSimilarMovies: LiveData<String>
        get() = _noSimilarMovies

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String>
        get() = _responseMessage

    private val _isLoadingDetails = MutableLiveData(false)
    val isLoadingDetails: LiveData<Boolean>
        get() = _isLoadingDetails

    private val _isLoadingSimilarMovies = MutableLiveData(false)
    val isLoadingSimilarMovies: LiveData<Boolean>
        get() = _isLoadingSimilarMovies

    private val _isFavourite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean>
        get() = _isFavourite


    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _isLoadingDetails.value = true
            networkRepo.getMovieDetails(movieId)
                .collect { response ->
                    if (response.isSuccess()) {
                        _movieDetails.value = response.data?.convertToMovieDetails()
                        Timber.i("Network request in details")
                    } else {
                        _responseMessage.value =
                            defineErrorType(response.error ?: ErrorEntity.Unknown)
                        _responseMessage.value = response.error.toString()
                    }
                    _isLoadingDetails.postValue(false)
                }
        }
    }


    fun getSimilarMovies(movieId: Int) {
        if (_isLoadingSimilarMovies.value!!.not() && canLoadMore()) {
            viewModelScope.launch {
                _isLoadingDetails.postValue(true)
                networkRepo
                    .getSimilarMovies(movieId, currentPage)
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
                            Timber.i("Network request in similar")
                        } else {
                            _responseMessage.value =
                                defineErrorType(response.error ?: ErrorEntity.Unknown)
                        }
                        _isLoadingSimilarMovies.postValue(false)
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

    fun addToFavourites(movie: Movie) {
        viewModelScope.launch {
            checkIfFavourite(movie.id)
            if (_isFavourite.value!!.not()) {
                databaseRepo.saveMovie(movie)
                _isFavourite.postValue(true)
            } else {
                databaseRepo.deleteMovie(movie)
                _isFavourite.postValue(false)
            }
        }
    }

    fun checkIfFavourite(movieId: Int) = viewModelScope.launch {
        databaseRepo.isExist(movieId).collect {
            _isFavourite.postValue(it)
        }
    }
}