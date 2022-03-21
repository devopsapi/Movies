package com.example.moviedb.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.databinding.MovieItemBinding
import com.example.moviedb.data.model.Movie
import java.util.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var movies: MutableList<Movie> = ArrayList()
    var onMovieItemClickListener: OnMovieItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MoviesAdapter.MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(MovieItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MoviesAdapter.MovieViewHolder, position: Int) {
        if (position != holder.absoluteAdapterPosition) return
        if (movies.size <= position) return
        val item = movies[position]
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(items: List<Movie>?) {
        movies.clear()
        val oldSize = items?.size
        items?.let {
            movies.addAll(it)
        }
        if (oldSize != null) {
            notifyItemRangeChanged(oldSize, items.size)
        }
    }


    inner class MovieViewHolder(private val binding: MovieItemBinding) :
        BaseViewHolder<Movie>(binding.root) {
        override fun onBind(item: Movie) {
            binding.apply {

                Glide.with(root)
                    .load(IMAGE_URL + item.poster_path)
                    .error(R.drawable.no_poster)
                    .into(moviePoster)

                root.setOnClickListener {
                    onMovieItemClickListener?.onItemClick(item)
                }
            }
        }
    }

    interface OnMovieItemClickListener {
        fun onItemClick(item: Movie)
    }

    companion object {
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
    }
}