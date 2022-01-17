package com.example.moviedb.viewmodels

import androidx.lifecycle.ViewModel

class PageViewModel : ViewModel() {

    var currentPage = 1
        private set

    fun updatePage() {
        currentPage++
    }

    fun decreasePage(){
        currentPage--
    }
}