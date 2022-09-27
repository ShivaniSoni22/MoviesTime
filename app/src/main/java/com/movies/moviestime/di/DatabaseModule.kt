package com.movies.moviestime.di

import android.app.Application
import androidx.room.Room
import com.movies.moviestime.database.main.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(application, MoviesDatabase::class.java, "movies_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideAllMoviesDao(moviesDatabase: MoviesDatabase) =
        moviesDatabase.allMoviesDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope