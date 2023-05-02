package com.example.storiee.view.register

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storiee.ViewModelFactory
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.model.UserModel
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
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {

        binding.registerRegisterBtn.setOnClickListener {
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()

                if(!formValidation()){
                    Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                }else{
                    registerViewModel.saveUser(UserModel(name, email, password, false))
                    registerViewModel.register(name, email, password)

                    registerViewModel.message.observe(this) { messageStatus ->
                        if (messageStatus == "User created"){
                            AlertDialog.Builder(this).apply {
                                setTitle("Mantap")
                                setMessage("Register berhasil")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                        }else if (messageStatus == "Email is already taken"){
                            Toast.makeText(this@RegisterActivity, messageStatus.toString(), Toast.LENGTH_LONG).show()
                        }else{
                            Log.e("Cek status", messageStatus)
                        }
                    }

                }
        }
    }

    private fun formValidation() : Boolean{

        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString().trim()

        var valid = false

        binding.edRegisterName.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.edRegisterNameCons.error = "Nama tidak boleh kosong"
            }else{
                binding.edRegisterNameCons.error = null
            }
        }

        binding.edRegisterEmail.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.edRegisterEmailCons.error = "Email tidak boleh kosong"
            }else if (!text.isValidEmail()){
                binding.edRegisterEmailCons.error = "Email tidak valid"
            } else{
                binding.edRegisterEmailCons.error = null
            }
        }

        binding.edRegisterPassword.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                binding.edRegisterPasswordCons.error = "Password tidak boleh kosong"
            }else if (text.length < 8){
                binding.edRegisterPasswordCons.error = "Password tidak boleh kurang dari 8 karakter"
            }else{
                binding.edRegisterPasswordCons.error = null
            }
        }

        if (binding.edRegisterNameCons.error == null && binding.edRegisterEmailCons.error == null && binding.edRegisterPasswordCons.error == null){
            valid = true
        }

        //Empty Check
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            valid = false
        }

        return valid
    }

    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}