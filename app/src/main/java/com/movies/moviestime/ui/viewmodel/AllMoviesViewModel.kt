package com.movies.moviestime.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movies.moviestime.data.AllMoviesRepository
import com.movies.moviestime.database.entity.model.Search
import com.movies.moviestime.database.entity.model.responses.MovieResponse
import com.movies.moviestime.network.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class AllMovieViewModel @Inject constructor(private val allMoviesRepository: AllMoviesRepository) :
    ViewModel() {

    private val _snackErrorMessage = MutableSharedFlow<String>()
    val snackErrorMessage = _snackErrorMessage.asSharedFlow()

    private val refreshTriggerChannel = Channel<RefreshLoad>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    private val _searchAllMovie = MutableLiveData<ApiStatus<MovieResponse>>()
    val searchAllMovies: LiveData<ApiStatus<MovieResponse>> = _searchAllMovie

    var searchMoviePage = 1
    var searchMoviesResponse: MovieResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    val getRecentMovie = refreshTrigger.flatMapLatest { refresh ->
        allMoviesRepository.getAllMovies(
            forceRefresh = refresh == RefreshLoad.FORCE,
            onFetchSuccess = {
                //do something
            },
            onFetchFailed = {
                viewModelScope.launch {
                    _snackErrorMessage.emit(it.toString())
                }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    enum class RefreshLoad {
        FORCE, NORMAL
    }

    fun getLatestPopularMovie() {
        if (getRecentMovie.value !is ApiStatus.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(RefreshLoad.FORCE)
            }
        }
    }

    fun onRefreshSwiped() {
        if (getRecentMovie.value !is ApiStatus.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(RefreshLoad.FORCE)
            }
        }
    }

    fun doSearchForMovie(q: String) {
        viewModelScope.launch {
            newSearchQuery = q
            _searchAllMovie.postValue(ApiStatus.Loading(data = null))
            try {
                val response = allMoviesRepository.searchForMovieItem(q, searchMoviePage)
                if (searchMoviesResponse == null || newSearchQuery != oldSearchQuery) {
                    searchMoviePage = 1
                    oldSearchQuery = newSearchQuery
                    searchMoviesResponse = response.data
                } else {
                    searchMoviePage++
                    val oldMovies = searchMoviesResponse?.Search
                    val newMovies = response.data?.Search
                    if (newMovies != null) {
                        oldMovies?.addAll(newMovies)
                    }
                }
                _searchAllMovie.postValue(ApiStatus.Success(searchMoviesResponse ?: response.data))
            } catch (e: Exception) {
                ApiStatus.Error(data = null, throwable = e)
            }
        }
    }

}