package com.dicoding.storyapp.view.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.dicoding.storyapp.R
import com.dicoding.storyapp.core.utils.UIText
import com.dicoding.storyapp.core.utils.observeData

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.view.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestLocationPermission =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupAction()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAction() {
        observeData(viewModel.state) {
            when(it) {
                is MapsState.OnLoading -> {
                    binding.progressBarMap.isVisible = true
                }
                is MapsState.ShowMessage -> {
                    showMessage(it.uiText)
                }

            }
        }
        observeData(viewModel.resultStories) { data ->
            data?.let {
                for (story in it) {
                    val latLng = LatLng(
                        (story.lat).toDouble(),
                        (story.lon).toDouble()
                    )
                    val marker = MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                    mMap.addMarker(marker)
                }
                binding.progressBarMap.isVisible = false
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyLocation()
        viewModel.getStories()
    }

    private fun getMyLocation() {
        val isGranted = ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted)
            mMap.isMyLocationEnabled = true
        else
            requestLocationPermission.launch(REQUIRED_PERMISSION)
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
        private const val REQUIRED_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}