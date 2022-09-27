package com.movies.moviestime.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_Storage")

@Singleton
class DataStorageImplementation @Inject constructor(@ApplicationContext context: Context) :
    DataStorage {

    private val dataStore = context.dataStore

    private object PreferenceKeys {
        val SELECTED_THEME = stringPreferencesKey("selected_theme")
        val IS_USER_FIRST_TIME = booleanPreferencesKey("user_first_time")
    }

    override fun selectedTheme() = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[PreferenceKeys.SELECTED_THEME] ?: "light"
    }

    override suspend fun setSelectedTheme(theme: String) {
        dataStore.edit {
            it[PreferenceKeys.SELECTED_THEME] = theme
        }
    }

    override fun isUserFirstTime(): Flow<Boolean> = dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[PreferenceKeys.IS_USER_FIRST_TIME] ?: true
    }

    override suspend fun setUserFirstTime(isFirstTime: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.IS_USER_FIRST_TIME] = isFirstTime
        }
    }

}