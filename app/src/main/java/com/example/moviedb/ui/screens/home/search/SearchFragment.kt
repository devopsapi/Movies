package com.example.moviedb.ui.screens.home.search

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.data.model.Movie
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.ui.adapters.MoviesAdapter
import com.example.moviedb.ui.screens.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private var moviesAdapter = MoviesAdapter()
    private var query = ""
    private val key = "Query"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        query = requireArguments().getString(key) ?: ""
        searchViewModel.searchForMovie(query)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpRecyclerView()
        observeData()
    }

    private fun setUpAdapter() {
        moviesAdapter.onMovieItemClickListener =
            object : MoviesAdapter.OnMovieItemClickListener {
                override fun onItemClick(item: Movie) {
                    this@SearchFragment.findNavController()
                        .navigate(
                            HomeFragmentDirections.actionHomeToMovieDetailFragment(
                                item.id, item.poster_path ?: "")
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

                    if (dy > 0) {

                        val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                        val lastVisibleItemPosition =
                            (layoutManager as GridLayoutManager).findLastVisibleItemPosition()


                        Timber.i("--------------------------")
                        Timber.i(" TOTAL$totalItemCount")
                        Timber.i("LAST_VISIBLE_ITEM: $lastVisibleItemPosition")

                        if ((lastVisibleItemPosition + 1) >= totalItemCount) {
                            searchViewModel.searchForMovie(query)
                        }
                    }
                }
            })
        }
    }

    private fun observeData() {
        searchViewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                if (it) {
                    binding.progressBar.visibility =
                        View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            })

            movieList.observe(viewLifecycleOwner, {
                moviesAdapter.setData(it)
            })
        }
    }
}