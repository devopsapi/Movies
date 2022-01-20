package com.example.moviedb.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedb.ui.adapter.MoviesAdapter
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.viewmodels.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var moviesAdapter = MoviesAdapter()
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: FragmentMovieListBinding
    private val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nestedScrollView = binding.scrollView
        progressBar = binding.progressBar

        setUpRecyclerView()
        observeData()
        setUpScroll()

        movieListViewModel.getPopularMovies()
    }

    private fun observeData() {
        movieListViewModel.movieList.observe(viewLifecycleOwner, {
            updateViews(it)
        })
    }

    private fun setUpRecyclerView() {
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = moviesAdapter

        }

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
        progressBar.visibility = View.GONE
        moviesAdapter.setData(movieList)
    }

    private fun setUpScroll() {
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v != null) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    progressBar.visibility = View.VISIBLE
                    movieListViewModel.updatePage()
                    movieListViewModel.getPopularMovies()
                }
            }
        })
    }
}