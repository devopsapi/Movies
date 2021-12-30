package com.example.moviedb.screens

import com.example.moviedb.constants.Credentials
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedb.App
import com.example.moviedb.adapter.MoviesAdapter
import com.example.moviedb.api.MovieApi
import com.example.moviedb.databinding.FragmentSimilarMovieListBinding
import com.example.moviedb.model.MovieModel
import com.example.moviedb.response.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SimilarMovieListFragment : Fragment() {

    @Inject
    lateinit var movieApi: MovieApi

    private var moviesAdapter = MoviesAdapter()
    private lateinit var binding: FragmentSimilarMovieListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSimilarMovieListBinding.inflate(inflater)

        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = moviesAdapter
        }

        val safeArgs: SimilarMovieListFragmentArgs by navArgs()

        getSimilarMovies(safeArgs.movieId)

        return binding.root
    }

    private fun getSimilarMovies(movieId: Int) {
        val responseCall = movieApi.getSimilarMovies(movieId, Credentials.API_KEY)

        responseCall.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                if (response.isSuccessful) {
                    updateData(response.body()?.popularMovies)
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.i("SIMILAR_MOVIES_FRAGMENT", "ERROR: " + t.message)
            }
        })
    }

    private fun updateData(movieList: List<MovieModel>?){
        context?.let {
            moviesAdapter.apply {
                setData(movieList)
                onMovieItemClickListener = object : MoviesAdapter.OnMovieItemClickListener {
                    override fun onItemClick(item: MovieModel) {
                        this@SimilarMovieListFragment.findNavController()
                            .navigate(
                                SimilarMovieListFragmentDirections
                                    .actionSimilarMovieListFragmentToMovieDetailFragment(item.id)
                            )
                    }
                }
            }
        }
    }
}
