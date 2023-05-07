package com.example.storiee.view.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.storiee.StoryPagingSource
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.model.UserModel
import com.example.storiee.data.response.AllStoriesResponse
import com.example.storiee.data.response.ListStoryItem
import com.example.storiee.data.response.LoginResult
import com.example.storiee.di.Injection
import com.example.storiee.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

//    private val _stories = MutableLiveData<List<ListStoryItem>>()
//    val stories: LiveData<List<ListStoryItem>> = _stories

//    fun getUser(): LiveData<UserModel> {
//        return pref.getUser().asLiveData()
//    }
//
//    fun getUserSession(): LiveData<LoginResult> {
//        return pref.getUserSession().asLiveData()
//    }
//
//    fun logout() {
//        viewModelScope.launch {
//            pref.logout()
//        }
//    }

//    fun getAllStories(token: String){
//
//        ApiConfig.instance.getAllStories(token).enqueue(object : Callback<AllStoriesResponse> {
//
//            override fun onResponse(call: Call<AllStoriesResponse>, response: Response<AllStoriesResponse>) {
//
//                if (response.isSuccessful){
//                    _stories.value = response.body()?.listStory
//                }
//            }
//
//            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
//                Log.e("MAIN ERROR", t.message.toString())
//            }
//
//        })
//    }
//
//    private val client = ApiConfig.getApiService()
//
//    fun getAllStories(token: String): LiveData<PagingData<ListStoryItem>> {
//        Log.e("cekkkkk33333", token)
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            pagingSourceFactory = {
//                StoryPagingSource(client, token)
//            }
//        ).liveData
//
//    }

    val stories : LiveData<PagingData<ListStoryItem>> = storyRepository.getAllStories().cachedIn(viewModelScope)
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