package com.example.moviedb.ui.screens

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.ui.adapter.MoviesAdapter
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.viewmodels.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var moviesAdapter = MoviesAdapter()
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: FragmentMovieListBinding
    private val movieListViewModel: MovieListViewModel by viewModels()
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieListViewModel.getPopularMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        setUpAdapter()
        setUpRecyclerView()
        observeData()
    }

    private fun observeData() {
        movieListViewModel.movieList.observe(viewLifecycleOwner, {
            updateViews(it)
        })
    }

    private fun setUpRecyclerView() {
        binding.rvMovies.apply {
            layoutManager =
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(context, 2)
                } else {
                    GridLayoutManager(context, 4)
                }
            adapter = moviesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                    val lastVisibleItemPosition =
                        (layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                    Log.i("POPULAR_MOVIES_TAG", "--------------------------")
                    Log.i("POPULAR_MOVIES_TAG", "TOTAL_ITEM_COUNT: $totalItemCount")
                    Log.i("POPULAR_MOVIES_TAG", "LAST_VISIBLE_ITEM: $lastVisibleItemPosition")

                    if (!loading) {
                        if ((lastVisibleItemPosition + 1) >= totalItemCount) {
                            loading = true

                            if (movieListViewModel.canLoadMore()) {
                                movieListViewModel.updatePage()
                                movieListViewModel.getPopularMovies()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun setUpAdapter() {
        moviesAdapter.onMovieItemClickListener = object : MoviesAdapter.OnMovieItemClickListener {
            override fun onItemClick(item: MovieModel) {
                this@MovieListFragment.findNavController()
                    .navigate(
                        MovieListFragmentDirections
                            .actionMovieListFragmentToMovieDetailFragment(item.id)
                    )
            }
        }
    }

    private fun updateViews(movieList: List<MovieModel>) {
        loading = false
        progressBar.visibility = View.GONE
        moviesAdapter.setData(movieList, false)
    }
}
