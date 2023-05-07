package com.example.storiee.data.local

import android.content.Context

class Preference(context: Context) {
    private val preference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val edit = preference.edit()
        edit.putString(TOKEN, token)
        edit.apply()
    }

    fun getToken(): String? {
        return preference.getString(TOKEN, null)
    }

    fun clearToken() {
        val edit = preference.edit().clear()
        edit.apply()
    }

    companion object {
        const val PREF_NAME = "login_pref"
        const val TOKEN = "token"
    }
}