package com.movies.moviestime.database.entity.model.responses

data class ErrorResponse(
    val code: String,
    val message: String,
    val status: String
)