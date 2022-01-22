package com.example.moviedb.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.ui.adapter.MoviesAdapter
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.viewmodels.SimilarMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimilarMovieListFragment : Fragment() {

    private var moviesAdapter = MoviesAdapter()
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: FragmentMovieListBinding
    private val similarMovieViewModel: SimilarMovieViewModel by viewModels()
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val safeArgs: SimilarMovieListFragmentArgs by navArgs()
        similarMovieViewModel.setMovieId(safeArgs.movieId)
        similarMovieViewModel.getSimilarMovies()
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

    private fun setUpAdapter() {
        moviesAdapter.onMovieItemClickListener = object : MoviesAdapter.OnMovieItemClickListener {
            override fun onItemClick(item: MovieModel) {
                this@SimilarMovieListFragment.findNavController()
                    .navigate(
                        SimilarMovieListFragmentDirections
                            .actionSimilarMovieListFragmentToMovieDetailFragment(item.id)
                    )
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = moviesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                    val lastVisibleItemPosition =
                        (layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                    Log.i("SIMILAR_MOVIES_TAG", "--------------------------")
                    Log.i("SIMILAR_MOVIES_TAG", "TOTAL_ITEM_COUNT: $totalItemCount")
                    Log.i("SIMILAR_MOVIES_TAG", "LAST_VISIBLE_ITEM: $lastVisibleItemPosition")

                    if (!loading) {
                        if ((lastVisibleItemPosition + 1) >= totalItemCount) {
                            loading = true

                            if (similarMovieViewModel.canLoadMore()) {
                                similarMovieViewModel.updatePage()
                                similarMovieViewModel.getSimilarMovies()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun observeData() {
        similarMovieViewModel.movieList.observe(viewLifecycleOwner, {
            updateData(it)
        })
    }

    private fun updateData(movieList: List<MovieModel>) {
        loading = false
        progressBar.visibility = View.GONE
        moviesAdapter.setData(movieList)
    }
}

