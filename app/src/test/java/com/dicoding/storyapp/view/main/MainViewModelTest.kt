package com.dicoding.storyapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.data.DataDummy
import com.dicoding.storyapp.data.model.Story
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.response.story.StoriesResponse.Companion.toDomain
import com.dicoding.storyapp.utils.MainCoroutineRule
import com.dicoding.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var storyRepository : StoryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `when Get all stories successfully` () = runTest {
        val dummyData = DataDummy.generateStoriesResponse().listStory.toDomain()
        val data = StoriesPagingSource.snapshot(dummyData)
        val expectedResult = MutableLiveData<PagingData<Story>>()
        expectedResult.value = data

        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedResult)
        mainViewModel = MainViewModel(userRepository, storyRepository)

        val actualResult = mainViewModel.getStoriesList().getOrAwaitValue()


        val differ = AsyncPagingDataDiffer(
            diffCallback = Story.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualResult)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyData.size, differ.snapshot().size)
        Assert.assertEquals(dummyData[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get all stories data is empty` () = runTest {
        val dummyData = emptyList<Story>()
        val data = StoriesPagingSource.snapshot(dummyData)
        val expectedResult = MutableLiveData<PagingData<Story>>()
        expectedResult.value = data

        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedResult)
        mainViewModel = MainViewModel(userRepository, storyRepository)

        val actualResult = mainViewModel.getStoriesList().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = Story.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualResult)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoriesPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}