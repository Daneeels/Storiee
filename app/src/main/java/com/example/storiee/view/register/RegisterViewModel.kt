package com.example.storiee.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storiee.data.api.ApiConfig
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.model.UserModel
import com.example.storiee.data.response.UserRegisterResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference) : ViewModel(){

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email:String, password: String ){

        _isLoading.value = true
        _message.value = ""

        ApiConfig.instance.register(name, email, password).enqueue(
            object : Callback<UserRegisterResponse> {

            override fun onResponse(call: Call<UserRegisterResponse>, response: Response<UserRegisterResponse>) {

                _isLoading.value = false

                if (response.isSuccessful){
                    _message.value = response.body()?.message
                    Log.e("REGISTER SUCCESS", _message.value.toString())
                }else{
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    _message.value = jObjError.getString("message").toString()
                }
            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                Log.e("REGISTER ERROR", t.message.toString())
            }

        })
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }



}