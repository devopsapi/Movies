package com.example.moviedb.ui.screens.home

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.ui.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TabMovieFragment : Fragment() {

    private var moviesAdapter = MoviesAdapter()
    private lateinit var binding: FragmentMovieListBinding

    private lateinit var movieViewModel: MovieViewModel

    @Inject
    lateinit var factory: MovieViewModelsFactory

    private var tabPosition = 0
    private val POSITION_KEY = "Current position"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        tabPosition = requireArguments().getInt(POSITION_KEY)
        movieViewModel = factory.getMovieViewModel(tabPosition)

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
                override fun onItemClick(item: MovieModel) {
                    this@TabMovieFragment.findNavController()
                        .navigate(
                            HomeFragmentDirections.actionHomeToMovieDetailFragment(
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

                    if (dy > 0) {

                        val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                        val lastVisibleItemPosition =
                            (layoutManager as GridLayoutManager).findLastVisibleItemPosition()


                        Timber.i("--------------------------")
                        Timber.i(" $totalItemCount")
                        Timber.i("LAST_VISIBLE_ITEM: $lastVisibleItemPosition")

                        if ((lastVisibleItemPosition + 1) >= totalItemCount) {
                            movieViewModel.getData()
                        }
                    }
                }
            })
        }
    }

    private fun observeData() {
        movieViewModel.apply {
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

            error.observe(viewLifecycleOwner, { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }
    }
}