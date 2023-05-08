package com.example.storiee.view.main

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.example.storiee.data.response.ListStoryItem
import com.example.storiee.di.Injection
import com.example.storiee.repository.StoryRepository

class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getAllStories().cachedIn(viewModelScope)

    class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(Injection.provideRepository(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}