package com.yusufbek.newsapp.ui.other

// To handle the loading and response states of the retrofit call
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : Resource<T>(data) // for successful answer
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message) // for failed response
    class Loading<T> : Resource<T>() // for loading state

}