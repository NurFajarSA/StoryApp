package com.dicoding.storyapp.data

import com.dicoding.storyapp.data.response.story.StoriesResponse
import com.dicoding.storyapp.data.response.story.StoryItem
import java.util.Date

object DataDummy {

    fun generateStoriesResponse() = StoriesResponse().apply {
        listStory = ArrayList<StoryItem>().apply {
            for (i in 0..9) {
                add(
                    StoryItem(
                        id = "story-FvU4u0Vp2S3PMsFg",
                        name = "Dimas",
                        description = "Lorem Ipsum",
                        photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                        createdAt = Date().toString(),
                        lat = -10.212F,
                        lon = -16.002F
                    )
                )
            }
        }
        error = false
        message = "success"
    }

}