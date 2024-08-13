package com.example.annaclinic.core.utils

import android.content.Context

class SharedPrefUtils(private val context: Context) {
    private fun pref() = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private fun edit() = pref().edit()

    fun remove(key: String) = edit().remove(key).apply()

    fun saveString(key: String, value: String?) = edit().putString(key, value).apply()

    fun getString(key: String) = pref().getString(key, null)

    fun saveBoolean(key: String, value: Boolean) = edit().putBoolean(key, value).apply()

    fun getBoolean(key: String) = pref().getBoolean(key, false)

    fun saveInt(key: String, value: Int) = edit().putInt(key, value).apply()

    fun getInt(key: String) = pref().getInt(key, 0)
}