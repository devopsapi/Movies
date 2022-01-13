package com.example.moviedb.screens

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
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
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    @Inject
    lateinit var movieApi: MovieApi

    lateinit var movieTitle: TextView
    lateinit var movieOverview: TextView
    lateinit var moviePoster: ImageView
    lateinit var movieReleaseDate: TextView
    lateinit var movieRating: TextView
    lateinit var movieVotes: TextView

    private lateinit var _binding: FragmentMovieDetailBinding

    private var movieId by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        val responseCall = movieApi.getMovieDetails(movieId, Credentials.API_KEY)

        responseCall.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    updateViews(response)
                }
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                Log.i("MOVIES_DETAILS_FRAGMENT", "ERROR: " + t.message)
            }
        })
    }


    private fun updateViews(response: Response<MovieDetailsResponse>) {
        with(response.body()!!) {

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