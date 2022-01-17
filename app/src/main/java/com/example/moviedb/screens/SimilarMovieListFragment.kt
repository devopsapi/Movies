package com.example.moviedb.screens

import com.example.moviedb.constants.Credentials
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedb.viewmodels.PageViewModel
import com.example.moviedb.adapter.MoviesAdapter
import com.example.moviedb.api.MovieApi
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.model.MovieModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


@AndroidEntryPoint
class SimilarMovieListFragment : Fragment(){

    @Inject
    lateinit var movieApi: MovieApi

    private var moviesAdapter = MoviesAdapter()

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var progressBar: ProgressBar

    private lateinit var binding: FragmentMovieListBinding

    private lateinit var pageViewModel: PageViewModel

    private lateinit var compositeDisposable: CompositeDisposable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMovieListBinding.inflate(inflater, container, false)

        nestedScrollView = binding.scrollView

        progressBar = binding.progressBar

        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = moviesAdapter
        }

        compositeDisposable = CompositeDisposable()

        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)

        val safeArgs: SimilarMovieListFragmentArgs by navArgs()

        setUpScroll(safeArgs.movieId)

        getSimilarMoviesFromNetwork(safeArgs.movieId, pageViewModel.currentPage)

        return binding.root
    }

    private fun getSimilarMoviesFromNetwork(movieId: Int, page: Int) {
        compositeDisposable.add(
            movieApi.getSimilarMovies(movieId, Credentials.API_KEY, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> updateData(response.movies) })
    }

    private fun updateData(movieList: List<MovieModel>) {

        progressBar.visibility = View.GONE

        moviesAdapter.apply {
            setData(movieList)
            onMovieItemClickListener = object : MoviesAdapter.OnMovieItemClickListener {
                override fun onItemClick(item: MovieModel) {
                    this@SimilarMovieListFragment.findNavController()
                        .navigate(
                            SimilarMovieListFragmentDirections
                                .actionSimilarMovieListFragmentToMovieDetailFragment(item.id)
                        )
                }
            }
        }
    }


    private fun setUpScroll(movieId: Int) {
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v != null) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    progressBar.visibility = View.VISIBLE
                    pageViewModel.updatePage()
                    getSimilarMoviesFromNetwork(movieId, pageViewModel.currentPage)
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}

