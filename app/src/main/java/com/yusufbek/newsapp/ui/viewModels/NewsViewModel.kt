package com.yusufbek.newsapp.ui.viewModels

import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.yusufbek.newsapp.ui.other.Resource
import com.yusufbek.newsapp.ui.repositories.NewsRepository
import com.yusufbek.newsapp.ui.retrofit.ArticleEntity
import com.yusufbek.newsapp.ui.retrofit.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNewsResult: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponse? = null

    val searchNewsResult: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        if (hasInternetConnection()) {
            newsRepository.getAllBreakingNews(countryCode, breakingNewsPage)
                .onStart {
                    breakingNewsResult.postValue(Resource.Loading())
                }
                .catch { error ->
                    when (error) {
                        is IOException -> breakingNewsResult.postValue(Resource.Error("Network failure"))
                        else -> breakingNewsResult.postValue(Resource.Error("Conversion error"))
                    }
                }.collect { newsResponse ->
                    breakingNewsPage++
                    if (breakingNewsResponse == null) {
                        breakingNewsResponse = newsResponse
                    } else {
                        val oldArticles = breakingNewsResponse?.articles
                        val newArticles = newsResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                    breakingNewsResult.postValue(
                        Resource.Success(
                            breakingNewsResponse ?: newsResponse
                        )
                    )
                }

        } else {
            breakingNewsResult.postValue(Resource.Error("No internet connection"))
        }
        //        Previous solution
//        breakingNewsResult.postValue(Resource.Loading())
//        try {
//            if (hasInternetConnection()) {
//                val response = newsRepository.getAllBreakingNews(countryCode, breakingNewsPage)
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        breakingNewsPage++
//                        if (breakingNewsResponse == null) {
//                            breakingNewsResponse = it
//                        } else {
//                            val oldArticles = breakingNewsResponse?.articles
//                            val newArticles = it.articles
//                            oldArticles?.addAll(newArticles)
//                        }
//
//                        breakingNewsResult.postValue(Resource.Success(breakingNewsResponse ?: it))
//                    }
//                } else {
//                    breakingNewsResult.postValue(Resource.Error(response.message()))
//                }
//            } else {
//                breakingNewsResult.postValue(Resource.Error("No internet connection"))
//            }
//        } catch (t: Throwable) {
//            when (t) {
//                is IOException -> breakingNewsResult.postValue(Resource.Error("Network failure"))
//                else -> breakingNewsResult.postValue(Resource.Error("Conversion error"))
//            }
//        }
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        if (hasInternetConnection()) {
            newsRepository.searchNews(searchQuery, searchNewsPage)
                .onStart {
                    searchNewsResult.postValue(Resource.Loading())
                }
                .catch { error ->
                    when (error) {
                        is IOException -> searchNewsResult.postValue(Resource.Error("Network failure"))
                        else -> searchNewsResult.postValue(Resource.Error("Conversion error"))
                    }
                }.collect { newsResponse ->
                    searchNewsPage++
                    if (searchNewsResponse == null) {
                        searchNewsResponse = newsResponse
                    } else {
                        val oldArticles = searchNewsResponse?.articles
                        val newArticles = newsResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                    searchNewsResult.postValue(
                        Resource.Success(
                            searchNewsResponse ?: newsResponse
                        )
                    )
                }

        } else {
            searchNewsResult.postValue(Resource.Error("No internet connection"))
        }

        //          Previous solution
//        searchNewsResult.postValue(Resource.Loading())
//        try {
//            if (hasInternetConnection()) {
//                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        searchNewsPage++
//                        if (searchNewsResponse == null) {
//                            searchNewsResponse = it
//                        } else {
//                            val oldArticles = searchNewsResponse?.articles
//                            val newArticles = it.articles
//                            oldArticles?.addAll(newArticles)
//                        }
//
//                        searchNewsResult.postValue(Resource.Success(searchNewsResponse ?: it))
//                    }
//                } else {
//                    searchNewsResult.postValue(Resource.Error(response.message()))
//                }
//            } else {
//                searchNewsResult.postValue(Resource.Error("No internet connection"))
//            }
//        } catch (t: Throwable) {
//            when (t) {
//                is IOException -> searchNewsResult.postValue(Resource.Error("Network failure"))
//                else -> searchNewsResult.postValue(Resource.Error("Conversion error"))
//            }
//        }
    }

    fun saveArticle(articleEntity: ArticleEntity) = viewModelScope.launch {
        newsRepository.insertOrUpdate(articleEntity)
    }

    fun getSavedNews(): LiveData<List<ArticleEntity>> = newsRepository.getSavedNews().asLiveData()

    fun deleteArticle(articleEntity: ArticleEntity) = viewModelScope.launch {
        newsRepository.deleteArticle(articleEntity)
    }

    private fun hasInternetConnection(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo.run {
                return when (this!!.type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }

}