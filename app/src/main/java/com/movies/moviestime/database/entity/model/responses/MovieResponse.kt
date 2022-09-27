package com.movies.moviestime.database.entity.model.responses

import com.google.gson.annotations.SerializedName
import com.movies.moviestime.database.entity.model.Search

data class MovieResponse(
    @SerializedName("Response")
    var Response: String,

    @SerializedName("Search")
    var Search: MutableList<Search>,

    @SerializedName("totalResults")
    var totalResults: String
)