package com.example.storiee.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.response.AllStoriesResponse
import com.example.storiee.data.response.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserPreference) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories


    fun getAllStoriesLocation(token: String) {

        ApiConfig.getApiService().getAllStoriesLocation(token, 1)
            .enqueue(object : Callback<AllStoriesResponse> {

                override fun onResponse(
                    call: Call<AllStoriesResponse>,
                    response: Response<AllStoriesResponse>
                ) {

                    if (response.isSuccessful) {
                        _stories.value = response.body()?.listStory
                    }
                }

                override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                    Log.e("MAIN ERROR", t.message.toString())
                }

            })
    }
}