package com.movies.moviestime.data

import com.movies.moviestime.network.ApiService
import com.movies.moviestime.utils.Constant
import javax.inject.Inject

class ApiDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllMovies(page: Int?) =
        apiService.getAllMovies(page = page ?: 1)

    suspend fun searchForMovie(q: String, page: Int?) =
        apiService.getSearchedMovie(searchTitle = q, page = page ?: 1)

}