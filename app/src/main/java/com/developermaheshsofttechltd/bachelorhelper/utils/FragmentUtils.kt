package com.developermaheshsofttechltd.bachelorhelper.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.developermaheshsofttechltd.bachelorhelper.R

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
