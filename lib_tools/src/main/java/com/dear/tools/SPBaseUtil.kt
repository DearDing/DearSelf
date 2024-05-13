package com.dear.tools

import android.content.Context
import android.content.SharedPreferences

class SPBaseUtil {

    companion object {

        @JvmStatic
        fun getSharedPreference(context: Context, name: String): SharedPreferences {
            return context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }

        @JvmStatic
        fun getSharedPreferenceEditor(context: Context, name: String): SharedPreferences.Editor {
            return getSharedPreference(context, name).edit()
        }

        @JvmStatic
        fun saveData(context: Context, name: String, key: String, value: Any) {
            val editor = getSharedPreferenceEditor(context, name)
            when(value) {
                 is Boolean -> {
                    editor.putBoolean(key, value)
                }
                 is Float -> {
                     editor.putFloat(key, value)
                 }
                 is Long -> {
                     editor.putLong(key, value)
                 }
                 is Int -> {
                     editor.putInt(key, value)
                 }
                else -> {
                    editor.putString(key, "$value")
                }
            }
            editor.apply()
        }

        @JvmStatic
        fun getString(
            context: Context,
            name: String,
            key: String,
            defaultValue: String = ""
        ): String {
            return getSharedPreference(context, name).getString(key, defaultValue) ?: defaultValue
        }

        @JvmStatic
        fun getInt(
            context: Context,
            name: String,
            key: String,
            defaultValue: Int = -1
        ): Int {
            return getSharedPreference(context, name).getInt(key, defaultValue)
        }

        @JvmStatic
        fun getFloat(
            context: Context,
            name: String,
            key: String,
            defaultValue: Float = -1f
        ): Float {
            return getSharedPreference(context, name).getFloat(key, defaultValue)
        }

        @JvmStatic
        fun getBoolean(
            context: Context,
            name: String,
            key: String,
            defaultValue: Boolean = false
        ): Boolean {
            return getSharedPreference(context, name).getBoolean(key, defaultValue)
        }

        @JvmStatic
        fun getLong(
            context: Context,
            name: String,
            key: String,
            defaultValue: Long = -1L
        ): Long {
            return getSharedPreference(context, name).getLong(key, defaultValue)
        }

    }
}