package com.example.storiee.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
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
        formValidation()
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

        binding.loginLoginBtn.setOnClickListener {

            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if(!formValidation()){
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }else{

                loginViewModel.loginRequest(email,password)

                loginViewModel.message.observe(this) { messageStatus ->
                    if (messageStatus == "success"){
                        loginViewModel.login()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }else if (messageStatus == "User not found"){
                        Toast.makeText(this@LoginActivity, messageStatus.toString(), Toast.LENGTH_LONG).show()
                    }else{
                        Log.e("Cek status", messageStatus)
                    }
                }

            }
        }
    }

    private fun formValidation() : Boolean{

        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()

        var valid = false

        binding.edLoginEmail.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.edLoginEmailCons.error = "Email tidak boleh kosong"
            }else if (!text.isValidEmail()){
                binding.edLoginEmailCons.error = "Email tidak valid"
            } else{
                binding.edLoginEmailCons.error = null
            }
        }

        binding.edLoginPassword.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.edLoginPasswordCons.error = "Password tidak boleh kosong"
            }else if (text.length < 8){
                binding.edLoginPasswordCons.error = "Password tidak boleh kurang dari 8 karakter"
            }else{
                binding.edLoginPasswordCons.error = null
            }
        }

        if (binding.edLoginEmailCons.error == null && binding.edLoginPasswordCons.error == null){
            valid = true
        }

        //Empty Check
        if (email.isEmpty() || password.isEmpty()){
            valid = false
        }
        return valid
    }

    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
