package com.example.moviedb.di

import com.example.moviedb.screens.MovieDetailFragment
import com.example.moviedb.screens.MovieListFragment
import com.example.moviedb.screens.SimilarMovieListFragment
import dagger.Component
import javax.inject.Singleton


@Component(modules = [NetworkModule::class])
@Singleton
interface AppComponent {
    fun inject(fragment: MovieListFragment)
    fun inject(fragment: MovieDetailFragment)
    fun inject(fragment: SimilarMovieListFragment)
}