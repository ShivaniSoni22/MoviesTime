package com.movies.moviestime.ui.viewmodel

import androidx.lifecycle.*
import com.movies.moviestime.storage.DataStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(private val dataStorage: DataStorage) : ViewModel() {

    val selectedTheme = dataStorage.selectedTheme().asLiveData()
    val isUsersFirstTime = dataStorage.isUserFirstTime().asLiveData()
    private val _hasClickedRetryButton = MutableLiveData<Boolean>()
    val hasClickedRetryButton: LiveData<Boolean> = _hasClickedRetryButton

    fun changeSelectedTheme(theme: String) {
        viewModelScope.launch {
            dataStorage.setSelectedTheme(theme)
        }
    }

    fun changeUsersFirstTime(isFirstTime: Boolean) {
        viewModelScope.launch {
            dataStorage.setUserFirstTime(isFirstTime)
        }
    }

    fun userClickedRetryButton(isClicked: Boolean) {
        _hasClickedRetryButton.value = isClicked
    }

}