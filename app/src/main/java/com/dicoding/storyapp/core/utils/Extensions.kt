package com.dicoding.storyapp.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.response.BaseResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

fun <T, U> Response<T>.asFlowStateEvent(mapper: (T) -> U): Flow<Resource<U>> {
    return flow {
        emit(Resource.OnLoading())
        val emitData = try {
            val body = body()
            if (isSuccessful && body != null) {
                val dataMapper = mapper.invoke(body)
                Resource.OnSuccess(dataMapper)
            } else {
                Resource.OnError(UIText.DynamicText(message()))
            }
        } catch (e: Throwable) {
            val errorMessage : UIText = when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<BaseResponse>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<BaseResponse>() {}.type
                        )
                        UIText.DynamicText(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.error)
                    }
                }
                else -> UIText.StringResource(R.string.error)
            }
            Resource.OnError(errorMessage)
        }
        emit(emitData)
    }
}

inline fun <T, U> Response<T>.asFlowStateEventWithAction(
    crossinline mapper: (T) -> U,
    crossinline action: suspend (T) -> Unit,
): Flow<Resource<U>> {
    return flow {
        emit(Resource.OnLoading())
        try {
            val body = body()
            if (isSuccessful && body != null) {
                val dataMapper = mapper.invoke(body)
                action.invoke(body)
                emit(Resource.OnSuccess(dataMapper))
            } else {
                emit(Resource.OnError(UIText.DynamicText(message())))
            }
        } catch (e: Throwable) {
            val errorMessage : UIText = when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<BaseResponse>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<BaseResponse>() {}.type
                        )
                        UIText.DynamicText(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.error)
                    }
                }
                else -> UIText.StringResource(R.string.error)
            }
            emit(Resource.OnError(errorMessage))
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

fun <T> LifecycleOwner.observeData(observable: LiveData<T>, observer: (T) -> Unit) {
    observable.observe(this) {
        observer(it)
    }
}