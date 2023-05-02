package com.example.storiee.view.main

import android.util.Log
import androidx.lifecycle.*
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.model.UserModel
import com.example.storiee.data.response.AllStoriesResponse
import com.example.storiee.data.response.ListStoryItem
import com.example.storiee.data.response.LoginResult
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getUserSession(): LiveData<LoginResult> {
        return pref.getUserSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }


    fun getAllStories(token: String){

        ApiConfig.instance.getAllStories(token).enqueue(object : Callback<AllStoriesResponse> {

            override fun onResponse(call: Call<AllStoriesResponse>, response: Response<AllStoriesResponse>) {

                if (response.isSuccessful){
                    _stories.value = response.body()?.listStory
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                Log.e("MAIN ERROR", t.message.toString())
            }

        })
    }

}