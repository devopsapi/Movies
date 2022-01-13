package com.example.moviedb.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.PageViewModel
import com.example.moviedb.adapter.MovieAdapter
import com.example.moviedb.api.MovieApi
import com.example.moviedb.constants.Credentials
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.model.MovieModel
import com.example.moviedb.response.MoviesResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment(), MovieAdapter.OnMovieClickListener {

    @Inject
    lateinit var movieApi: MovieApi

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: MovieAdapter

    lateinit var nestedScrollView: NestedScrollView

    lateinit var progressBar: ProgressBar

    private lateinit var _binding: FragmentMovieListBinding

    private lateinit var pageViewModel: PageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)

        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        val view = _binding.root

        nestedScrollView = _binding.scrollView

        progressBar = _binding.progressBar

        recyclerView = _binding.recyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        getMoviesFromNetwork(pageViewModel.currentPage)

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v != null) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    progressBar.visibility = View.VISIBLE
                    pageViewModel.updatePage()

                    getMoviesFromNetwork(pageViewModel.currentPage)
                }
            }
        })

        return view
    }

    private fun getMoviesFromNetwork(page: Int) {

        val responseCall = movieApi.getPopularMovies(Credentials.API_KEY, page)

        responseCall.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                if (response.isSuccessful) {

                    updateViews(response)
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.i("MOVIES_LIST_FRAGMENT", "ERROR: " + t.message)
            }
        })
    }

    private fun updateViews(response: Response<MoviesResponse>) {

        progressBar.visibility = View.GONE

        val movieList = response.body()?.popularMovies

        adapter = context?.let {
            movieList?.let { it1 ->
                MovieAdapter(
                    it1,
                    this@MovieListFragment
                )
            }
        }!!

        recyclerView.adapter = adapter
    }

    override fun onMovieClick(movie: MovieModel) {
        view?.findNavController()
            ?.navigate(
                MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(
                    movie.id
                )
            )
    }
}