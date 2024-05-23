package com.dicoding.storyapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dicoding.storyapp.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class DataPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    val token: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }

    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[USER_ID] = user.userId
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[USER_ID] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = ""
            preferences[TOKEN_KEY] = ""
            preferences[USER_ID] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataPreferences? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID = stringPreferencesKey("userId")

        fun getInstance(dataStore: DataStore<Preferences>): DataPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = DataPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}