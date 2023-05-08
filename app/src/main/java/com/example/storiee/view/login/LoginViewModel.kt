package com.example.storiee.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.response.LoginResult
import com.example.storiee.data.response.UserLoginResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _data = MutableLiveData<LoginResult>()
    val data: LiveData<LoginResult> = _data

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginRequest(email: String, password: String) {

        _isLoading.value = true
        _message.value = ""

        ApiConfig.getApiService().login(email, password)
            .enqueue(object : Callback<UserLoginResponse> {

                override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>
                ) {

                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _message.value = response.body()?.message
                        _data.value = response.body()?.loginResult

                        Log.e("LOGIN SUCCESS", _message.value.toString())

                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        _message.value = jObjError.getString("message").toString()
                    }

                }

                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    Log.e("LOGIN ERROR", t.message.toString())
                }

            })
    }


}