package com.example.storiee.view.detail

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storiee.R
import com.example.storiee.ViewModelFactory
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.response.Story
import com.example.storiee.databinding.ActivityDetailBinding
import com.example.storiee.view.main.MainAdapter.Companion.ID_KEY

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var storyIdData: String
    private lateinit var token: String
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        val storyId = intent.getStringExtra(ID_KEY).toString()
        storyIdData = storyId

//        detailViewModel.getUserSession().observe(this) { token ->
//            this.token = token.token
//        }

        val pref = com.example.storiee.data.local.Preference(applicationContext)
        detailViewModel.getDetailStory(pref.getToken().toString(), storyIdData)

        detailViewModel.story.observe(this) { story ->
            setUserData(story)
        }

    }

    private fun setUserData(_user: Story) {
        binding.apply {
            Glide.with(this@DetailActivity).load(_user.photoUrl).into(imageDetailIV)
            nameDetailTV.text = _user.name
            storyDescTV.text = _user.description
        }
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailViewModel::class.java]

    }
}
