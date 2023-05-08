package com.example.storiee.view.register

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storiee.ViewModelFactory
import com.example.storiee.data.local.UserPreference
import com.example.storiee.databinding.ActivityRegisterBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }


    private fun setupAction() {

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.registerRegisterBtn.isEnabled =
                    binding.edRegisterPassword.text?.length!! >= 8
            }

            override fun afterTextChanged(s: Editable) {
                binding.registerRegisterBtn.isEnabled =
                    binding.edRegisterPassword.text?.length!! >= 8
            }
        })



        binding.registerRegisterBtn.setOnClickListener {

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (!(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())) {
                Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            } else {

                registerViewModel.register(name, email, password)

                registerViewModel.message.observe(this) { messageStatus ->
                    when (messageStatus) {
                        "User created" -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Mantap")
                                setMessage("Register berhasil")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        "Email is already taken" -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                messageStatus.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            Log.e("Cek status", messageStatus)
                        }
                    }
                }

            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}