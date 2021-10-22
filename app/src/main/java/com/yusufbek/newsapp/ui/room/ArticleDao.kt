package com.yusufbek.newsapp.ui.room

import androidx.room.*
import com.yusufbek.newsapp.ui.retrofit.ArticleEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(article: ArticleEntity): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

}