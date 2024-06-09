package com.developermaheshapps.bachelorhelper.utils
import android.content.Context
import com.google.gson.Gson

object SharedPreferencesHelper {
    private const val PREF_NAME = "my_prefs"
    private const val KEY_STRING_LIST = "titleList"

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveStringList(context: Context, stringList: ArrayList<String>) {
        val gson = Gson()
        val json = gson.toJson(stringList)
        getSharedPreferences(context).edit().putString(KEY_STRING_LIST, json).apply()
    }

    fun getStringList(context: Context): ArrayList<String> {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(KEY_STRING_LIST, null)
        return gson.fromJson(json, ArrayList<String>()::class.java) ?: ArrayList()
    }


    private const val KEY_BOOK_LIST = "bookList"

    fun saveBookList(context: Context, stringList: ArrayList<String>) {
        val gson = Gson()
        val json = gson.toJson(stringList)
        getSharedPreferences(context).edit().putString(KEY_BOOK_LIST, json).apply()
    }

    fun getBookList(context: Context,isFragment:Boolean=false): ArrayList<String> {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(KEY_BOOK_LIST, null)

        return gson.fromJson(json, ArrayList<String>()::class.java) ?: ArrayList()
    }
}
