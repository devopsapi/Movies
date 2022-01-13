package com.example.moviedb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.model.MovieModel

class MovieAdapter(
    private val movies: List<MovieModel>,
    private val mOnMovieClickListener: OnMovieClickListener
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    private lateinit var context: Context

    class MyViewHolder(
        private val itemView: View,
        private val movies: List<MovieModel>,
        private val mOnMovieClickListener: OnMovieClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)

        init {
            moviePoster.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.let { mOnMovieClickListener.onMovieClick(movies[position]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        context = parent.context
        return MyViewHolder(view, movies, mOnMovieClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500" + movies[position].poster_path)
            .into(holder.moviePoster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnMovieClickListener {
       fun onMovieClick(movie: MovieModel)
    }
}