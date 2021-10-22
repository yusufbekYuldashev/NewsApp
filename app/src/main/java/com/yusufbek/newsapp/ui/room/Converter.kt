package com.yusufbek.newsapp.ui.room

import androidx.room.TypeConverter
import com.yusufbek.newsapp.ui.retrofit.Source

class Converter {

    @TypeConverter
    fun fromSource(source: Source) = source.name

    @TypeConverter
    fun toSource(name: String) = Source(name, name)

}