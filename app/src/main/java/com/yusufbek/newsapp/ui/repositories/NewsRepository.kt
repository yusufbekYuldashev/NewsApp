package com.yusufbek.newsapp.ui.repositories

import com.yusufbek.newsapp.ui.retrofit.ArticleEntity
import com.yusufbek.newsapp.ui.retrofit.NewsApi
import com.yusufbek.newsapp.ui.retrofit.NewsResponse
import com.yusufbek.newsapp.ui.room.ArticleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApi
) {

    //Conventional style
//    suspend fun getAllBreakingNews(countryCode: String, pageNumber: Int) =
//        api.getAllBreakingNews(countryCode, pageNumber)

    suspend fun getAllBreakingNews(countryCode: String, pageNumber: Int): Flow<NewsResponse> {
        return flow {
            val response = api.getAllBreakingNews(countryCode, pageNumber)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }


//    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
//        api.searchForNews(searchQuery, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Flow<NewsResponse> {
        return flow {
            val response = api.searchForNews(searchQuery, pageNumber)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun insertOrUpdate(articleEntity: ArticleEntity) =
        dao.insertOrUpdate(articleEntity)

    fun getSavedNews(): Flow<List<ArticleEntity>> = dao.getAllArticles()

    suspend fun deleteArticle(articleEntity: ArticleEntity) =
        dao.deleteArticle(articleEntity)
}