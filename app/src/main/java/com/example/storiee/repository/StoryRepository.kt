package com.example.storiee.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storiee.StoryPagingSource
import com.example.storiee.data.api.ApiService
import com.example.storiee.data.response.ListStoryItem

class StoryRepository(
    private val apiService: ApiService,
    private val pref: com.example.storiee.data.local.Preference
) {

    val token = pref.getToken()

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token.toString())
            }
        ).liveData
    }

}