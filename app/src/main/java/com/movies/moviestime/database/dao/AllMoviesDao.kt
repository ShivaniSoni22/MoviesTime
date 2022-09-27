package com.movies.moviestime.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.moviestime.database.entity.model.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface AllMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllMovies(allMovies: List<Search>)

    @Query("SELECT * FROM searched_movies")
    fun getAllMovies(): PagingSource<Int, Search>

    @Query("SELECT * FROM searched_movies")
    fun getAllUpdatedMovies(): Flow<List<Search>>

    @Query("DELETE FROM searched_movies")
    suspend fun deleteAllMovies()

}