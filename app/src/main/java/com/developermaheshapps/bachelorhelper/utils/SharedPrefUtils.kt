package com.developermaheshapps.bachelorhelper.utils

import android.content.Context
import android.content.SharedPreferences
import com.developermaheshapps.bachelorhelper.R

object SharedPrefUtils {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        preferences =
            context.getSharedPreferences(
                "${
                    context.getString(R.string.app_name).replace(" ", "_")
                }_prefs", Context.MODE_PRIVATE
            )
    }

    fun getPrefString(key: String, defaultValue: String): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getPrefBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getPrefInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun putPrefString(key: String, value: String): Boolean {
        editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
        return true
    }

    fun putPrefInt(key: String, value: Int): Boolean {
        editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
        return true
    }

    fun putPrefBoolean(key: String, value: Boolean): Boolean {
        editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
        return true
    }
}

object SharedPrefKeys {
    const val COURSE_NAME="COURSE_NAME"
    const val BTECH = "BTECH"
    const val BE = "BE"
    const val BCA = "BCA"
    const val BBA = "BBA"
    const val BSC = "BSC"
    const val BED = "BED"
    const val BPHARM = "BPHARM"
    const val BCOM = "BCOM"
    const val BBACA = "BBACA"
    const val BA = "BA"
}