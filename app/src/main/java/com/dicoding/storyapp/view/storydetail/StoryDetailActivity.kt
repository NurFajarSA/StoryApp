package com.dicoding.storyapp.view.storydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.IntentCompat
import com.dicoding.storyapp.data.model.Story
import com.dicoding.storyapp.databinding.ActivityStoryDetailBinding
import com.squareup.picasso.Picasso

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val story = IntentCompat.getParcelableExtra(intent, EXTRA_STORY, Story::class.java)
        binding.tvDetailName.text = story?.name
        binding.tvDetailDescription.text = story?.description
        Picasso.with(this)
            .load(story?.photoUrl)
            .into(binding.ivDetailPhoto)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}