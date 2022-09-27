package com.movies.moviestime.data

import android.provider.SyncStateContract
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.movies.moviestime.database.entity.model.responses.MovieResponse
import com.movies.moviestime.database.entity.model.Search
import com.movies.moviestime.network.ApiStatus
import com.movies.moviestime.network.BaseDataSource
import com.movies.moviestime.database.main.MoviesDatabase
import com.movies.moviestime.utils.Constant
import com.movies.moviestime.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ExperimentalPagingApi
class AllMoviesRepository @Inject constructor(
    private val apiDataSource: ApiDataSource,
    private val moviesDatabase: MoviesDatabase
) : BaseDataSource() {

    private val allMoviesDao = moviesDatabase.allMoviesDao()

    fun getAllMovies(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<ApiStatus<List<Search>>> =
        networkBoundResource(
            query = {
                allMoviesDao.getAllUpdatedMovies()
            },
            fetch = {
                val response = apiDataSource.getAllMovies(null)
                response.Search
            },
            saveFetchResult = { allMovies ->
                val popularMovies = allMovies.map { it }
                moviesDatabase.withTransaction {
                    allMoviesDao.deleteAllMovies()
                    allMoviesDao.saveAllMovies(popularMovies)
                }
            },
            shouldFetch = {
                forceRefresh
            },
            onFetchSuccess = onFetchSuccess,
            onFetchFailed = { t ->
                if (t !is HttpException && t !is IOException) {
                    throw t
                }
                onFetchFailed(t)
            }
        )

    suspend fun searchForMovieItem(q: String, page: Int?): ApiStatus<MovieResponse> {
        return safeApiCall {
            apiDataSource.searchForMovie(q, page)
        }
    }

}