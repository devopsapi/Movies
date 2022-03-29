package com.example.moviedb.ui.screens.moviedetails

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.data.api.responses.MovieDetails
import com.example.moviedb.data.model.Movie
import com.example.moviedb.databinding.FragmentMovieDetailBinding
import com.example.moviedb.ui.adapters.DefaultLoadStateAdapter
import com.example.moviedb.ui.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private var moviesAdapter = MoviesAdapter()
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.ViewHolder

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()
    private val safeArgs: MovieDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieDetailsViewModel.getMovieDetails(safeArgs.movieId)
        movieDetailsViewModel.checkIfFavourite(safeArgs.movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        mainLoadStateHolder = DefaultLoadStateAdapter.ViewHolder(binding.loadStateView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setupRecyclerView()
        observeData()
        observeLoadState()

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.favourite.setOnClickListener {
            val movie = Movie(safeArgs.movieId, safeArgs.poster)
            movieDetailsViewModel.addToFavourites(movie)
        }
    }

    private fun observeData() {
        movieDetailsViewModel.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                getSimilarMovies(safeArgs.movieId).collectLatest { movies ->
                    moviesAdapter.submitData(movies)
                }
            }

            movieDetails.observe(viewLifecycleOwner, {
                updateMovieDetails(it)
            })

            responseMessage.observe(viewLifecycleOwner, { errorMessage ->
                binding.root.visibility = View.GONE
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })

            noSimilarMovies.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    binding.similarMovies.text = it
                }
            })

            isFavourite.observe(viewLifecycleOwner) {
                if (it) {
                    binding.heartImg.setImageResource(R.drawable.red_heart)
                } else {
                    binding.heartImg.setImageResource(R.drawable.white_heart)
                }
            }
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            moviesAdapter.loadStateFlow.collectLatest { state ->
                mainLoadStateHolder.bind(state.refresh)
            }
        }
    }

    private fun updateMovieDetails(movieDetails: MovieDetails) {
        with(movieDetails) {
            binding.apply {

                movieTitle.text = title
                movieOverview.text = overview
                movieReleaseDate.text = releaseDate
                movieRating.text = voteAverage

                val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

                for (genre in genres) {
                    if (genres.indexOf(genre) == 0) layoutParams.marginStart =
                        0 else layoutParams.marginStart = 30
                    val genreTab = TextView(context)
                    genreTab.text = genre.name
                    genreTab.layoutParams = layoutParams
                    genreTab.setTextAppearance(R.style.movieOverview)
                    binding.layoutForGenres.addView(genreTab)
                }

                context?.let {
                    Glide.with(it)
                        .load(URL + poster_path)
                        .error(R.drawable.no_poster)
                        .into(moviePoster)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvMovies.apply {
            val layout = LinearLayoutManager(context)
            layout.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = layout
            adapter = moviesAdapter.withLoadStateFooter(DefaultLoadStateAdapter())
        }
    }

    private fun setUpAdapter() {
        moviesAdapter.onMovieItemClickListener =
            object : MoviesAdapter.OnMovieItemClickListener {
                override fun onItemClick(item: Movie) {
                    this@MovieDetailFragment.findNavController()
                        .navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(
                            item.id, item.poster_path))
                }
            }
    }

    companion object {
        const val URL = "https://image.tmdb.org/t/p/w500"
    }
}

