package com.movies.moviestime.network

sealed class ApiStatus<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {

    class Loading<T>(data: T? = null) : ApiStatus<T>(data)
    class Success<T>(data: T?) : ApiStatus<T>(data)
    class Error<T>(data: T? = null, throwable: Throwable) : ApiStatus<T>(data, throwable)

}