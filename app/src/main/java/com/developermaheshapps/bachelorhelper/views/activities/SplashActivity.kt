package com.developermaheshapps.bachelorhelper.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.ActivitySplashBinding
import com.developermaheshapps.bachelorhelper.utils.Constants
import com.developermaheshapps.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshapps.bachelorhelper.utils.UpdateManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SharedPrefUtils.init(activity)
        activity.window.statusBarColor = getColor(R.color.gray)

        val apkFile = UpdateManager(activity).apkFile
        if (apkFile.exists()) {
            apkFile.delete()
        }
        FirebaseDatabase.getInstance().getReference("AppImpData").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = ArrayList<String>()
                    for (i in snapshot.children) {
                        tempList.add(i.value.toString())
                    }
                    Log.d("VISHAY", "onDataChange:$tempList ")
//                    val url = snapshot.getValue(String::class.java)
                    SharedPrefUtils.putPrefString("apkUrl", tempList[0])
                    SharedPrefUtils.putPrefString("baseUrl", tempList[1])
                    SharedPrefUtils.putPrefBoolean("isCompulsory", tempList[2].toBoolean())
                    SharedPrefUtils.putPrefString("driveApkUrl", tempList[3])
                    SharedPrefUtils.putPrefString("version", tempList[4])

                    if (Constants.isConnected(activity)) {

                        binding.apply {
                            Log.d(
                                "VISHAY",
                                "onCreate:${
                                    SharedPrefUtils.getPrefString("profilePic", "").toString()
                                } "
                            )
                            if (!SharedPrefUtils.getPrefBoolean("isLogin", false)) {
                                SharedPrefUtils.putPrefString("name", "Guestxxxx9164")
                                Handler(Looper.myLooper()!!).postDelayed({
                                    startActivity(Intent(activity, OnboardingActivity::class.java))
                                    finish()
                                }, 1000)
                            } else {
                                Handler(Looper.myLooper()!!).postDelayed({
                                    startActivity(Intent(activity, HomeActivity::class.java))
                                    finish()
                                }, 1000)
                            }

                        }


                    } else {
                        Constants.checkInternet(activity, true)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }
}
