package com.dicoding.storyapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.data.local.DataPreferences
import com.dicoding.storyapp.data.remote.StoryService
import com.dicoding.storyapp.data.response.story.StoryItem
import kotlinx.coroutines.flow.first

class StoriesPagingSource(
    private val service: StoryService,
    private val dataStore: DataPreferences
) : PagingSource<Int, StoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val token = dataStore.token.first()
            val bearerToken = "Bearer $token"
            val page = params.key ?: 1

            val response = service.getStories(
                bearerToken = bearerToken,
                page = page,
                size = 20,
                location = 1
            )

            val dataList = response.body()?.listStory ?: emptyList()
            LoadResult.Page(
                data = dataList,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (dataList.isEmpty()) null else page + 1
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? =
        state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}