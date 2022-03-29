package com.example.moviedb.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.DefaultLoadStateBinding
import com.example.moviedb.utils.GeneralErrorHandlerImpl
import com.example.moviedb.utils.defineErrorType

class DefaultLoadStateAdapter : LoadStateAdapter<DefaultLoadStateAdapter.ViewHolder>() {

    class ViewHolder(private val binding: DefaultLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.errorText.isVisible = loadState is LoadState.Error
            if (loadState is LoadState.Error) {
                binding.errorText.text =
                    defineErrorType(GeneralErrorHandlerImpl().getError(loadState.error))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DefaultLoadStateBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}