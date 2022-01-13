package com.example.moviedb.screens

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.api.MovieApi
import com.example.moviedb.constants.Credentials
import com.example.moviedb.databinding.FragmentMovieDetailBinding
import com.example.moviedb.response.MovieDetailsResponse
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    @Inject
    lateinit var movieApi: MovieApi

    private lateinit var movieTitle: TextView
    private lateinit var movieOverview: TextView
    private lateinit var moviePoster: ImageView
    private lateinit var movieReleaseDate: TextView
    private lateinit var movieRating: TextView
    private lateinit var movieVotes: TextView

    private lateinit var _binding: FragmentMovieDetailBinding

    private var movieId = 0

    private lateinit var compositeDisposable: CompositeDisposable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        compositeDisposable = CompositeDisposable()

        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        val view = _binding.root

        movieId = MovieDetailFragmentArgs.fromBundle(requireArguments()).movieId

        movieTitle = _binding.movieTitle

        movieOverview = _binding.movieOverview
        movieOverview.movementMethod = ScrollingMovementMethod.getInstance()

        moviePoster = _binding.moviePoster

        movieReleaseDate = _binding.movieReleaseDate

        movieRating = _binding.movieRating

        movieVotes = _binding.movieVotes

        getMovieDetailsFromNetwork(movieId)

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.similarMovieListFragment -> findNavController().navigate(
                MovieDetailFragmentDirections.actionMovieDetailFragmentToSimilarMovieListFragment(
                    movieId
                )
            )
        }

        return false
    }


    private fun getMovieDetailsFromNetwork(movieId: Int) {
        compositeDisposable.add(movieApi.getMovieDetails(movieId, Credentials.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { movieDetails -> updateViews(movieDetails) })
    }


    private fun updateViews(movieDetails: MovieDetailsResponse) {
        with(movieDetails) {

            movieTitle.text = title

            movieOverview.text = overview

            movieReleaseDate.text = release_date

            movieRating.text = popularity.toString()

            movieVotes.text = vote_count.toString()

            context?.let {
                Glide.with(it)
                    .load("https://image.tmdb.org/t/p/w500$poster_path")
                    .into(moviePoster)

            }
        }
    }
}