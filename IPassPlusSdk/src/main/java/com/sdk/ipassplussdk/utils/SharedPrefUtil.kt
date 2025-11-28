package com.sdk.ipassplussdk.utils

import android.content.Context

class SharedPrefUtil(private val context: Context) {

    private val prefs = context.getSharedPreferences("iPassPlus_preferences", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return prefs.getString(key, defaultValue)
    }

    fun setDbType(value: String) {
        prefs.edit().putString("database_id", value).apply()
    }

    fun getDbType(): String? {
        return prefs.getString("database_id", "")
    }

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return prefs.getInt(key, defaultValue)
    }

    // Add similar methods for other data types (Boolean, Float, Long, etc.)

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}