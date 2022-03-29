package com.example.moviedb.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.databinding.MovieItemBinding
import com.example.moviedb.data.model.Movie

class MoviesAdapter : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffCallBack()) {

    var onMovieItemClickListener: OnMovieItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MoviesAdapter.MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(MovieItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MoviesAdapter.MovieViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.onBind(item)
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

class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}