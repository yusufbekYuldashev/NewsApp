package com.yusufbek.newsapp.ui.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yusufbek.newsapp.ui.retrofit.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1
)
@TypeConverters(
    Converter::class
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

//    companion object {
//        @Volatile
//        private var instance: ArticleDatabase? = null
//        private val LOCK = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: createDatabase(context).also {
//                instance = it
//            }
//        }
//
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                ArticleDatabase::class.java,
//                "article_db.db"
//            ).build()
//    }
}