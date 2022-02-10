package com.example.moviedb.ui.screens.movie_details

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.data.api.responses.MovieDetailsResponse
import com.example.moviedb.data.model.MovieModel
import com.example.moviedb.databinding.FragmentMovieDetailBinding
import com.example.moviedb.ui.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private var moviesAdapter = MoviesAdapter()
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()
    private val similarMovieViewModel: SimilarMovieViewModel by viewModels()
    private val safeArgs: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setupRecyclerView()
        observeData()

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        movieDetailsViewModel.getMovieDetails(safeArgs.movieId)
        similarMovieViewModel.getSimilarMovies(safeArgs.movieId)
    }

    private fun observeData() {
        movieDetailsViewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                if (it) {
                    binding.progressBar.visibility =
                        View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            })

            movieDetails.observe(viewLifecycleOwner, {
                updateMovieDetails(it)
            })

            error.observe(viewLifecycleOwner, { errorMessage ->
                binding.root.visibility = View.GONE
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }

        similarMovieViewModel.apply {
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

            noSimilarMovies.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    binding.similarMovies.text = it
                }
            })
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateMovieDetails(movieDetails: MovieDetailsResponse) {
        with(movieDetails) {
            binding.apply {
                movieTitle.text = title
                movieOverview.text = overview
                movieReleaseDate.text =
                    if (release_date.isNotEmpty()) "Release: $release_date" else "No date"
                movieRating.text = if (vote_average == null) "Rating: $vote_average" else "No votes"

                val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

                if (genres.isNotEmpty()) {
                    for (genre in genres) {
                        if (genres.indexOf(genre) == 0) layoutParams.marginStart =
                            0 else layoutParams.marginStart = 30
                        val genreTab = TextView(context)
                        genreTab.text = genre.name
                        genreTab.layoutParams = layoutParams
                        genreTab.setTextAppearance(R.style.movieOverview)
                        binding.layoutForGenres.addView(genreTab)
                    }
                } else {
                    val noGenre = TextView(context)
                    noGenre.text = "No genre(s)"
                    noGenre.setTextAppearance(R.style.movieOverview)
                    noGenre.layoutParams = layoutParams
                    binding.layoutForGenres.addView(noGenre)
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
                            similarMovieViewModel.getSimilarMovies(safeArgs.movieId)
                        }
                    }
                }
            })
        }
    }

    private fun setUpAdapter() {
        moviesAdapter.onMovieItemClickListener =
            object : MoviesAdapter.OnMovieItemClickListener {
                override fun onItemClick(item: MovieModel) {
                    this@MovieDetailFragment.findNavController()
                        .navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(item.id))
                }
            }
    }

    companion object {
        const val URL = "https://image.tmdb.org/t/p/w500"
    }
}