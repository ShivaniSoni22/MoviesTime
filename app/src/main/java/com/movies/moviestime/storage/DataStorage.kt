package com.movies.moviestime.storage

import kotlinx.coroutines.flow.Flow

interface DataStorage {

    fun selectedTheme() : Flow<String>
    suspend fun setSelectedTheme(theme: String)
    fun isUserFirstTime() : Flow<Boolean>
    suspend fun setUserFirstTime(isFirstTime: Boolean)

}