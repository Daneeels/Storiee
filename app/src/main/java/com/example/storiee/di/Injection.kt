package com.example.storiee.di

import android.content.Context
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val pref = com.example.storiee.data.local.Preference(context)
        return StoryRepository(apiService, pref)
    }
}