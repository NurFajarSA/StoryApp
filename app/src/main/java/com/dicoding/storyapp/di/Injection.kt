package com.dicoding.storyapp.di

import com.dicoding.storyapp.data.local.DataPreferences
import com.dicoding.storyapp.data.paging.StoriesPagingSource
import com.dicoding.storyapp.data.remote.Api
import com.dicoding.storyapp.data.repository.RepositoryImpl
import com.dicoding.storyapp.domain.repository.Repository

object Injection {

    fun providePaging(
        api: Api,
        dataPreferences: DataPreferences
    ): StoriesPagingSource {
        return StoriesPagingSource(
            api = api,
            dataStore = dataPreferences
        )
    }

    fun provideRepository(
        api: Api,
        dataPreferences: DataPreferences,
        pagingSource: StoriesPagingSource
    ): Repository {
        return RepositoryImpl(
            api = api,
            dataStore = dataPreferences,
            pagingSource = pagingSource
        )
    }
}