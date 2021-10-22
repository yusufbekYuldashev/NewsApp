package com.yusufbek.newsapp.ui.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.yusufbek.newsapp.ui.other.Constants
import com.yusufbek.newsapp.ui.retrofit.NewsApi
import com.yusufbek.newsapp.ui.room.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, ArticleDatabase::class.java, "article_db").build()

    @Singleton
    @Provides
    fun provideNewsDao(db: ArticleDatabase) = db.getArticleDao()

    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext app: Context) =
        app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}