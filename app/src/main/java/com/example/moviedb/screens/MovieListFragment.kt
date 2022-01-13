package com.example.moviedb.screens

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment(), MovieAdapter.OnMovieClickListener {

    @Inject
    lateinit var movieApi: MovieApi

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: MovieAdapter

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var progressBar: ProgressBar

    private lateinit var _binding: FragmentMovieListBinding

    private lateinit var pageViewModel: PageViewModel

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        compositeDisposable = CompositeDisposable()

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
        compositeDisposable.add(movieApi.getPopularMovies(Credentials.API_KEY, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response -> updateViews(response.movies) })
    }

    private fun updateViews(movieList: List<MovieModel>) {

        progressBar.visibility = View.GONE

        adapter = context?.let {
            MovieAdapter(
                movieList,
                this@MovieListFragment
            )
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