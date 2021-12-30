package com.example.moviedb

import android.app.Application
import com.example.moviedb.di.AppComponent
import com.example.moviedb.di.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}