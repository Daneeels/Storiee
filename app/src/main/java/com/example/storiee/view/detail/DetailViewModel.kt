package com.example.storiee.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.response.DetailStoryResponse
import com.example.storiee.data.response.LoginResult
import com.example.storiee.data.response.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val pref: UserPreference) : ViewModel() {

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> = _story

    fun getDetailStory(token: String, id: String) {

        ApiConfig.getApiService().getDetailStory("Bearer $token", id).enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {

                if (response.isSuccessful) {
                    _story.value = response.body()?.story
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e("On Failure", t.message.toString())
            }

        })
    }

    fun getUserSession(): LiveData<LoginResult> {
        return pref.getUserSession().asLiveData()
    }
}