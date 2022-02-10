package com.example.moviedb.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentHomeBinding
import com.example.moviedb.ui.screens.latest_movies.LatestMoviesFragment
import com.example.moviedb.ui.screens.now_playing_movies.NowPlayingMoviesFragment
import com.example.moviedb.ui.screens.popular_movies.PopularMoviesListFragment
import com.example.moviedb.ui.screens.search_movie.SearchFragment
import com.example.moviedb.ui.screens.top_rated_movies.TopRatedMoviesFragment
import com.example.moviedb.ui.screens.upcoming_movies.UpcomingMoviesFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var fragment: Fragment

    private var CURRENT_FRAGMENT_POSITION = 0
    private val POSITION_KEY = "Current position"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            CURRENT_FRAGMENT_POSITION = savedInstanceState.getInt(POSITION_KEY)
        }

        binding.tabsLayout.selectTab(binding.tabsLayout.getTabAt(CURRENT_FRAGMENT_POSITION))

        fragment = selectTabFragment(CURRENT_FRAGMENT_POSITION)
        fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        binding.tabsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select
                when (tab?.position) {
                    0 -> fragment = PopularMoviesListFragment()
                    1 -> fragment = LatestMoviesFragment()
                    2 -> fragment = NowPlayingMoviesFragment()
                    3 -> fragment = TopRatedMoviesFragment()
                    4 -> fragment = UpcomingMoviesFragment()
                }

                CURRENT_FRAGMENT_POSITION = tab?.position ?: 0

                fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameLayout, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                binding.tabsLayout.tabSelectedIndicator.alpha = 255
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselected
            }
        })

        setUpSearch()
    }


    private fun setUpSearch() {
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                @SuppressLint("ResourceAsColor")
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {

                        binding.tabsLayout.selectTab(null)
                        binding.tabsLayout.tabSelectedIndicator.alpha = 0

                        clearFocus()

                        val bundle = Bundle()
                        bundle.putString("Query", query)

                        fragment = SearchFragment()
                        fragment.arguments = bundle

                        fragmentTransaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frameLayout, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun selectTabFragment(position: Int): Fragment {
        return when (position) {
            0 -> PopularMoviesListFragment()
            1 -> LatestMoviesFragment()
            2 -> NowPlayingMoviesFragment()
            3 -> TopRatedMoviesFragment()
            4 -> UpcomingMoviesFragment()
            else -> PopularMoviesListFragment()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(POSITION_KEY, CURRENT_FRAGMENT_POSITION)
    }
}
