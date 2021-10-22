package com.yusufbek.newsapp.ui.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yusufbek.newsapp.ui.NewsApplication
import com.yusufbek.newsapp.ui.repositories.NewsRepository

//class NewsViewModelFactory(
//    private val app: Application,
//    private val newsRepository: NewsRepository
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return NewsViewModel(newsRepository) as T
//    }
//}