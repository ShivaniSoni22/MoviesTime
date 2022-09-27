package com.movies.moviestime.network

import com.movies.moviestime.database.entity.model.responses.MovieResponse
import com.movies.moviestime.utils.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("?type=movie")
    suspend fun getAllMovies(
        @Query(value = "s") searchTitle: String? = "Batman",
        @Query(value = "apiKey") apiKey: String = Constant.API_KEY,
        @Query(value = "page") page: Int,
    ): MovieResponse

    @GET("?type=movie")
    suspend fun getSearchedMovie(
        @Query(value = "s") searchTitle: String? = "Batman",
        @Query(value = "apiKey") apiKey: String = Constant.API_KEY,
        @Query(value = "page") page: Int,
    ): Response<MovieResponse>
}