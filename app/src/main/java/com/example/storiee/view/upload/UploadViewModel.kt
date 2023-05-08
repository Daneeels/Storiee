package com.example.storiee.view.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.response.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(private val pref: UserPreference) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(token :String, imageMultipart: MultipartBody.Part, description : RequestBody){

        _isLoading.value = true

        ApiConfig.getApiService().uploadStory("Bearer $token", imageMultipart, description).enqueue(object :
            Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {

                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.e("Cek 1", responseBody.message)
                    }

                } else {
                    Log.e("Cek 2", response.body()?.message.toString())
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.e("Upload error", t.message.toString())
            }
        })
    }

    fun uploadStoryLocation(token :String, imageMultipart: MultipartBody.Part, description : RequestBody, lat : Float, lon : Float){

        _isLoading.value = true

        ApiConfig.getApiService().uploadStoryLoc("Bearer $token", imageMultipart, description, lat, lon).enqueue(object :
            Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {

                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.e("Cek 1", responseBody.message)
                    }

                } else {
                    Log.e("Cek 2", response.body()?.message.toString())
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.e("Upload error", t.message.toString())
            }
        })
    }
}