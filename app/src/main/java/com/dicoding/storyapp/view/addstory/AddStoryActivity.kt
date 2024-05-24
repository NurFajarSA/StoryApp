package com.dicoding.storyapp.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.core.utils.UIText
import com.dicoding.storyapp.core.utils.getImageUri
import com.dicoding.storyapp.core.utils.observeData
import com.dicoding.storyapp.core.utils.uriToFile
import com.dicoding.storyapp.data.request.AddStoryRequest
import com.dicoding.storyapp.databinding.ActivityAddStoryBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.MainActivity
import kotlinx.coroutines.launch

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var descriptionEditText: AppCompatEditText
    private var currentImageUri: Uri? = null
    private val viewModel: AddStoryViewModel by viewModels<AddStoryViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        lifecycleScope.launch {
            viewModel.isSubmitEnabled.collect {
                binding.buttonAdd.isEnabled = it
            }
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        descriptionEditText = binding.edAddDescription
    }

    private fun setupAction() {
        observeData(viewModel.state) {
            when (it) {
                is AddStoryState.OnLoading -> {
                    binding.buttonAdd.isEnabled = !it.state
                }
                is AddStoryState.ShowMessage -> {
                    showMessage(it.uiText)
                }
                is AddStoryState.StoryUploaded -> {
                    Intent(this, MainActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
            }
        }

        binding.buttonCamera.setOnClickListener { startCamera() }
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { addStory() }

        binding.edAddDescription.addTextChangedListener {
            viewModel.setDescription(it.toString())
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showMessage("No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPhoto.setImageURI(it)
            viewModel.setFileUri(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun addStory() {
        val file = uriToFile(currentImageUri!!, this)
        viewModel.uploadStory(
            AddStoryRequest(
                description = binding.edAddDescription.text.toString(),
                photo = file,
                lat = null,
                lon = null
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showMessage(uiText: UIText) {
        when(uiText){
            is UIText.DynamicText -> {
                showMessage(uiText.value)
            }
            is UIText.StringResource -> {
                showMessage(getString(uiText.id))
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}