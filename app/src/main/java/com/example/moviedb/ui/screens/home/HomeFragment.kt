package com.example.moviedb.ui.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviedb.databinding.FragmentHomeBinding
import com.example.moviedb.ui.adapters.TabMoviesAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var tabsAdapter: TabMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabsAdapter = TabMoviesAdapter(this)
        binding.pager.adapter = tabsAdapter

        TabLayoutMediator(binding.tabsLayout, binding.pager) { tab, position ->
            tab.text = defineTabName(position)
        }.attach()
    }

    private fun defineTabName(position: Int): String {
        return when (position) {
            0 -> "Popular"
            1 -> "Latest"
            2 -> "Now playing"
            3 -> "Top rated"
            4 -> "Upcoming"
            5 -> "Favourites"
            else -> "Popular"
        }
    }
}