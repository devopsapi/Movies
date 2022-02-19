package com.example.moviedb.ui.screens.home

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
import com.example.moviedb.ui.screens.home.search.SearchFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var fragment: Fragment

    private var currentTabPosition = 0
    private val positionKey = "Current position"
    private val queryKey = "Query"

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
            currentTabPosition = savedInstanceState.getInt(positionKey)
        }

        binding.tabsLayout.selectTab(binding.tabsLayout.getTabAt(currentTabPosition))

        val bundle = Bundle()
        bundle.putInt(positionKey, currentTabPosition)

        fragment = TabMovieFragment()
        fragment.arguments = bundle
        fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        binding.tabsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select

                currentTabPosition = tab?.position ?: 0

                bundle.putInt(positionKey, currentTabPosition)

                fragment = TabMovieFragment()
                fragment.arguments = bundle
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
                        clearFocus()

                        binding.tabsLayout.selectTab(null)
                        binding.tabsLayout.tabSelectedIndicator.alpha = 0

                        val bundle = Bundle()
                        bundle.putString(queryKey, query)

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(positionKey, currentTabPosition)
    }
}