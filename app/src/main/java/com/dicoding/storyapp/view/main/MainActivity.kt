package com.dicoding.storyapp.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.addstory.AddStoryActivity
import com.dicoding.storyapp.view.main.adapter.StoriesAdapter
import com.dicoding.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvStories: RecyclerView
    private lateinit var storiesAdapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.getUser().observe(this) { user ->
            if (user.token == "") {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
        setupAdapter()
        getStoriesData()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        rvStories = binding.rvStories
        rvStories.setHasFixedSize(true)
        rvStories.layoutManager = LinearLayoutManager(this)
    }

    private fun setupAdapter() {
        storiesAdapter = StoriesAdapter()
        rvStories.adapter = storiesAdapter

        storiesAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun getStoriesData() {
        lifecycleScope.launch {
            viewModel.getStories().collectLatest { pagingData ->
                storiesAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupAction() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    viewModel.logout()
                }
            }
            true
        }
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }
}
