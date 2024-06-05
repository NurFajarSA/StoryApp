package com.dicoding.storyapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.data.local.DataPreferences
import com.dicoding.storyapp.data.model.Story
import com.dicoding.storyapp.data.remote.StoryService
import com.dicoding.storyapp.data.response.story.StoriesResponse.Companion.toDomain
import kotlinx.coroutines.flow.first

class StoriesPagingSource(
    private val service: StoryService,
    private val dataStore: DataPreferences
) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val token = dataStore.token.first()
            val bearerToken = "Bearer $token"
            val page = params.key ?: INITIAL_PAGE_INDEX

            val response = service.getStories(
                bearerToken = bearerToken,
                page = page,
                size = 10,
                location = 0
            )

            val dataList = response.body()?.listStory.toDomain()
            LoadResult.Page(
                data = dataList,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (dataList.isEmpty()) null else page + 1
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? =
        state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}