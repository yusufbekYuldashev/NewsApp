package com.yusufbek.newsapp.ui.retrofit

data class NewsResponse(
    val articles: MutableList<ArticleEntity>,
    val status: String,
    val totalResults: Int
)