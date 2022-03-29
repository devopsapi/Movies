package com.example.moviedb.ui.screens.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedb.data.model.Movie
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.ui.adapters.DefaultLoadStateAdapter
import com.example.moviedb.ui.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TabMovieFragment : Fragment() {
    private lateinit var movieListBinding: FragmentMovieListBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.ViewHolder
    private var moviesAdapter = MoviesAdapter()

    private lateinit var movieViewModel: MovieTabViewModel

    @Inject
    lateinit var factory: MovieViewModelFactory

    private var tabPosition = 0
    private val positionKey = "Current position"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabPosition = requireArguments().getInt(positionKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        movieListBinding = FragmentMovieListBinding.inflate(inflater, container, false)
        mainLoadStateHolder = DefaultLoadStateAdapter.ViewHolder(movieListBinding.loadStateView)
        movieViewModel =
            ViewModelProvider(requireActivity(), factory)[MovieTabViewModel::class.java]

        return movieListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpRecyclerView()
        observeData()
        observeLoadState()
    }

    private fun setUpAdapter() {
        moviesAdapter.onMovieItemClickListener =
            object : MoviesAdapter.OnMovieItemClickListener {
                override fun onItemClick(item: Movie) {
                    this@TabMovieFragment.findNavController()
                        .navigate(
                            HomeFragmentDirections.actionHomeToMovieDetailFragment(
                                item.id, item.poster_path
                            )
                        )
                }
            }
    }

    private fun setUpRecyclerView() {
        movieListBinding.rvMovies.apply {
            layoutManager =
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(context, 2)
                } else {
                    GridLayoutManager(context, 4)
                }
            adapter = moviesAdapter.withLoadStateFooter(DefaultLoadStateAdapter())
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            moviesAdapter.loadStateFlow.collectLatest { state ->
                mainLoadStateHolder.bind(state.refresh)
            }
        }
    }

    private fun observeData() {
        movieViewModel.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                movieViewModel.getMovies(tabPosition).collectLatest { movies ->
                    moviesAdapter.submitData(movies)
                }
            }
        }
    }
}