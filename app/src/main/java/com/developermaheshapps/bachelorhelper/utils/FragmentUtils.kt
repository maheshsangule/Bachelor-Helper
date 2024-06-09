package com.developermaheshapps.bachelorhelper.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.developermaheshapps.bachelorhelper.R

class FragmentUtils {
    companion object {
        fun loadFragment(fragment: Fragment, activity: AppCompatActivity) {
            try {
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commitNow()
            } catch (e: Exception) {
                Log.e("VISHAY", "Error replacing fragment: ${e.message}")
            }
        }

    }
}
