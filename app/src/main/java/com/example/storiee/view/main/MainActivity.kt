package com.example.storiee.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storiee.LoadingStateAdapter
import com.example.storiee.databinding.ActivityMainBinding
import com.example.storiee.view.maps.MapsActivity
import com.example.storiee.view.upload.UploadActivity
import com.example.storiee.view.welcome.WelcomeActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.ViewModelFactory(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()

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
    }

    private fun setupViewModel() {
        val pref = com.example.storiee.data.local.Preference(applicationContext)

        if (pref.getToken() != null) {

            val adapter = MainAdapter()
            val layoutManager = LinearLayoutManager(this)

            binding.mainRv.layoutManager = layoutManager
            binding.mainRv.adapter = adapter

            binding.mainRv.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )

            mainViewModel.stories.observe(this) {
                adapter.submitData(lifecycle, it)
            }

        } else {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    private fun setupAction() {
        binding.addBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        }

        binding.mapsBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.logOutBtn.setOnClickListener {
            val pref = com.example.storiee.data.local.Preference(applicationContext)
            pref.clearToken()

            val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
            startActivity(intent)
        }

    }

}