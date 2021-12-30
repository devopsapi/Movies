package com.example.moviedb.screens

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.moviedb.App
import com.example.moviedb.R
import com.example.moviedb.api.MovieApi
import com.example.moviedb.constants.Credentials
import com.example.moviedb.response.MovieDetailsResponse
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class MovieDetailFragment : Fragment() {

    @Inject
    lateinit var movieApi: MovieApi

    lateinit var movieTitle: TextView
    lateinit var movieOverview: TextView
    lateinit var moviePoster: ImageView
    lateinit var movieReleaseDate: TextView
    lateinit var movieRating: TextView
    lateinit var movieVotes: TextView

    var movieId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        movieId = MovieDetailFragmentArgs.fromBundle(requireArguments()).movieId

        movieTitle = view.findViewById(R.id.movie_title)

        movieOverview = view.findViewById(R.id.movie_overview)
        movieOverview.movementMethod = ScrollingMovementMethod.getInstance()

        moviePoster = view.findViewById(R.id.movie_poster)

        movieReleaseDate = view.findViewById(R.id.movie_release_date)

        movieRating = view.findViewById(R.id.movie_rating)

        movieVotes = view.findViewById(R.id.movie_votes)

        setUpDetailsView(movieId)

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

    private fun setUpDetailsView(movieId: Int) {

        val responseCall = movieApi.getMovieDetails(movieId, Credentials.API_KEY)

        responseCall.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                if (response.isSuccessful) {

                    movieTitle.text = response.body()?.title

                    movieOverview.text = response.body()?.overview

                    movieReleaseDate.text = response.body()?.release_date

                    movieRating.text = response.body()?.popularity.toString()

                    movieVotes.text = response.body()?.vote_count.toString()

                    context?.let {
                        Glide.with(it)
                            .load("https://image.tmdb.org/t/p/w500" + response.body()?.poster_path)
                            .into(moviePoster)
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                Log.i("MOVIES_DETAILS_FRAGMENT", "ERROR: " + t.message)
            }
        })
    }
}