package com.example.moviedb.ui.screens.moviedetails

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.moviedb.data.api.responses.MovieDetails
import com.example.moviedb.data.api.responses.convertToMovieDetails
import com.example.moviedb.data.model.Movie
import com.example.moviedb.data.repository.DatabaseRepository
import com.example.moviedb.data.repository.NetworkRepository
import com.example.moviedb.utils.ErrorEntity
import com.example.moviedb.utils.defineErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val networkRepo: NetworkRepository,
    private val databaseRepo: DatabaseRepository,
) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails>
        get() = _movieDetails

    private val _noSimilarMovies = MutableLiveData("")
    val noSimilarMovies: LiveData<String>
        get() = _noSimilarMovies

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String>
        get() = _responseMessage

    private val _isFavourite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean>
        get() = _isFavourite


    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
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
                }
        }
    }


    fun getSimilarMovies(movieId: Int): Flow<PagingData<Movie>> =
        networkRepo.getSimilarMovies(movieId)


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