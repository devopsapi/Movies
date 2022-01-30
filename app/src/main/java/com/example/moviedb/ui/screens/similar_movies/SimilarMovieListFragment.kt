package com.example.moviedb.ui.screens.similar_movies

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.ui.adapter.MoviesAdapter
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.data.model.MovieModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SimilarMovieListFragment : Fragment() {

    private var moviesAdapter = MoviesAdapter()
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: FragmentMovieListBinding
    private val safeArgs: SimilarMovieListFragmentArgs by navArgs()
    private val similarMovieViewModel: SimilarMovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        similarMovieViewModel.getSimilarMovies(safeArgs.movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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

    private fun setUpAdapter() {
        moviesAdapter.onMovieItemClickListener = object : MoviesAdapter.OnMovieItemClickListener {
            override fun onItemClick(item: MovieModel) {
                this@SimilarMovieListFragment.findNavController()
                    .navigate(
                        SimilarMovieListFragmentDirections.actionSimilarMovieListFragmentToMovieDetailFragment(
                            item.id
                        )
                    )
            }
        }
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

                    Timber.i("--------------------------")
                    Timber.i(" $totalItemCount")
                    Timber.i("LAST_VISIBLE_ITEM: $lastVisibleItemPosition")

                    if ((lastVisibleItemPosition + 1) >= totalItemCount) {
                        similarMovieViewModel.getSimilarMovies(safeArgs.movieId)
                    }
                }
            })
        }
    }

    private fun observeData() {
        similarMovieViewModel.apply {
            movieList.observe(viewLifecycleOwner, {
                updateData(it)
            })

            error.observe(viewLifecycleOwner, { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun updateData(movieList: List<MovieModel>) {
        moviesAdapter.setData(movieList)
    }
}

