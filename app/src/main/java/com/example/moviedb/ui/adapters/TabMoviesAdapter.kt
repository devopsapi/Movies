package com.example.moviedb.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moviedb.ui.screens.home.TabMovieFragment


class TabMoviesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val tabsAmount = 5
    private val positionKey = "Current position"

    override fun getItemCount(): Int = tabsAmount

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt(positionKey, position)

        val fragment = TabMovieFragment()
        fragment.arguments = bundle

        return fragment
    }
}