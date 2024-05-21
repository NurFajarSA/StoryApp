package com.dicoding.storyapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataPreferences(appContext: Context) {

    private val dataStore: DataStore<Preferences> = appContext.dataStore

    val token: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }

    val isLogin: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[IS_LOGIN] ?: "false"
        }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[IS_LOGIN] = "true"
        }

    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[IS_LOGIN] = "false"
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN = stringPreferencesKey("isLogin")
        private val Context.dataStore: DataStore<Preferences> by
        preferencesDataStore(name = "story_app")
    }
}