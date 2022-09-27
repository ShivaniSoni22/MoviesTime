package com.movies.moviestime.di

import com.movies.moviestime.storage.DataStorage
import com.movies.moviestime.storage.DataStorageImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    abstract fun bindDataStorage(dataStorageImplementation: DataStorageImplementation): DataStorage

}