package com.example.storiee.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storiee.R
import com.example.storiee.ViewModelFactory
import com.example.storiee.data.local.UserPreference
import com.example.storiee.databinding.ActivityLoginBinding
import com.example.storiee.view.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.loginLoginBtn.isEnabled = binding.edLoginPassword.text?.length!! >= 8
            }
            override fun afterTextChanged(s: Editable) {
                binding.loginLoginBtn.isEnabled = binding.edLoginPassword.text?.length!! >= 8
            }
        })

        binding.loginLoginBtn.setOnClickListener {

            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if(!(email.isNotEmpty() && password.isNotEmpty())){
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }else{

                if (password.length >= 8) {
                    loginViewModel.loginRequest(email,password)

                    loginViewModel.message.observe(this) { messageStatus ->
                        if (messageStatus == "success"){

                            loginViewModel.data.observe(this){session ->

                                val pref = com.example.storiee.data.local.Preference(applicationContext)
                                pref.saveToken(session.token)

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()

                            }
                        }else if (messageStatus == "User not found"){
                            Toast.makeText(this@LoginActivity, messageStatus.toString(), Toast.LENGTH_LONG).show()
                        }else{
                            Log.e("Cek status", messageStatus)
                        }
                    }
                }
                else{
                    Toast.makeText(
                        this@LoginActivity,
                        "Please make at least 8 characters for password",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
