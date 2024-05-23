package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.core.utils.asFlowStateEvent
import com.dicoding.storyapp.core.utils.asFlowStateEventWithAction
import com.dicoding.storyapp.data.local.DataPreferences
import com.dicoding.storyapp.data.remote.UserService
import com.dicoding.storyapp.data.response.login.LoginResponse.Companion.toDomain
import com.dicoding.storyapp.data.model.User
import com.dicoding.storyapp.data.request.LoginRequest
import com.dicoding.storyapp.data.request.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    val userService: UserService,
    val dataStore: DataPreferences
) {
    suspend fun doRegister(request: RegisterRequest): Flow<Resource<String>> =
        userService.register(
            request
        ).asFlowStateEvent(
            mapper = { it.message ?: "User Created" }
        )

    suspend fun doLogin(request: LoginRequest): Flow<Resource<User>> =
        userService.login(
            request
        ).asFlowStateEventWithAction(
            mapper = { it.toDomain() },
            action = {
                dataStore.saveSession(it.toDomain())
            }
        )

    suspend fun doLogout() {
        dataStore.logout()
    }

    fun getUser(): Flow<User> {
        return dataStore.getSession().map { it }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            dataPreference: DataPreferences,
            userService: UserService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(
                    userService = userService,
                    dataStore = dataPreference,
                )
            }.also { instance = it }
    }
}