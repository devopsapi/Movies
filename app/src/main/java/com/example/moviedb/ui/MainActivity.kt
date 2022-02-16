package com.example.moviedb.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.moviedb.R
import com.example.moviedb.ui.screens.OnBoardFragmentDirections
import com.example.moviedb.utils.Credentials.PREFERENCES_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val isFirstLaunch = sharedPref.getBoolean(PREFERENCES_KEY, true)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (isFirstLaunch.not()) {
            navController.navigate(OnBoardFragmentDirections.actionOnBoardToHome())
        }
    }
}