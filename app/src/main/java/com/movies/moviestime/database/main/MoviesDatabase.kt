package com.movies.moviestime.database.main

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movies.moviestime.database.dao.AllMoviesDao
import com.movies.moviestime.database.entity.model.Search

@Database(
    entities = [Search::class],
    version = 1
)
@TypeConverters(RoomConverter::class)
abstract class MoviesDatabase : RoomDatabase() {

//    abstract fun recentArticleDao(): RecentMoviesDao
    abstract fun allMoviesDao(): AllMoviesDao
//    abstract fun movieRemoteKeyDao(): RecentMoviesRemoteKeyDao

}