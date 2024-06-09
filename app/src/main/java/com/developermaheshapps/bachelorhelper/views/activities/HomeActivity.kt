package com.developermaheshapps.bachelorhelper.views.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.PackageInfoCompat
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.ActivityHomeBinding
import com.developermaheshapps.bachelorhelper.databinding.BottomsheetBackpressedBinding
import com.developermaheshapps.bachelorhelper.utils.Constants
import com.developermaheshapps.bachelorhelper.utils.Constants.STORAGE_PERMISSION_CODE
import com.developermaheshapps.bachelorhelper.utils.Constants.checkNotification
import com.developermaheshapps.bachelorhelper.utils.Constants.dismissPermissionDialog
import com.developermaheshapps.bachelorhelper.utils.Constants.isStoragePermissionGranted
import com.developermaheshapps.bachelorhelper.utils.Constants.setImage
import com.developermaheshapps.bachelorhelper.utils.Constants.showPermissionDialog
import com.developermaheshapps.bachelorhelper.utils.FragmentUtils.Companion.loadFragment
import com.developermaheshapps.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshapps.bachelorhelper.utils.UpdateManager
import com.developermaheshapps.bachelorhelper.views.fragments.BookmarkFragment
import com.developermaheshapps.bachelorhelper.views.fragments.DownloadFragment
import com.developermaheshapps.bachelorhelper.views.fragments.HomeFragment
import com.developermaheshapps.bachelorhelper.views.fragments.SearchFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val activity = this


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SharedPrefUtils.init(activity)
        UpdateManager(this).checkForUpdates()


        if (!SharedPrefUtils.getPrefBoolean("isUpdateAvailable", false)) {
            if (!SharedPrefUtils.getPrefBoolean("isLogin", false))
                Constants.userProfile(activity)
        }

//        val updateManager = UpdateManager(this)
//        updateManager.installApk()
        binding.apply {
//            bubbleTabBar.setSelectedWithId(R.id.menu_home,true)
            // Assuming this code is inside your activity or fragment
            chipNavigationBar.setItemSelected(R.id.menu_home, true)

            toolBar.setNavigationOnClickListener {
                drawerLayout.open()
            }

            tvProfile.setOnClickListener {

                Constants.userProfile(activity)
            }
            tvDisclaimar.setOnClickListener {
                MaterialAlertDialogBuilder(
                    this@HomeActivity,
                    R.style.CustomAlertDialogStyle
                ).apply {
                    setTitle("Disclaimar")
                    setMessage(
                        "\"Whatsapp\" and the \"Whatsapp\" name are copyrighted to WhatsApp, Inc. ©⚠" +
                                "This status video downloader app is not affiliated with, sponsored by, or endorsed by WhatsApp, Inc. \uD83D\uDEAB\uD83D\uDD04\uD83E\uDD1D" +
                                "Any use of downloaded content by the user is not the responsibility of this app. \uD83D\uDCE4\uD83D\uDEAB⚠\n"
                    )
                    setPositiveButton("Understand", null)
                    show()
                }
            }
            tvShare.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "\uD83C\uDF93 Unlock Your Bachelor Journey with Bachelor Helper! \uD83D\uDCDA Dive " +
                                "into a vast library of free books covering all bachelor streams, from self-improvement " +
                                "to career advancement. Find quick meal recipes, relationship advice, and more tailored" +
                                " to your bachelor lifestyle. Access exclusive features for organizing your schedule and" +
                                " discovering the hottest hangout spots. Connect with a thriving community of like-minded " +
                                "bachelors to share tips and experiences. Download Bachelor Helper now to embark on a" +
                                " journey of self-discovery, growth, and limitless possibilities! :${SharedPrefUtils.getPrefString("driveApkUrl","")}"
                    )
                    activity.startActivity(this)
                }
            }
            tvRateUs.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + activity.packageName)
                ).apply {
                    activity.startActivity(this)
                }
            }
            tvPrivacyPolicy.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://sites.google.com/view/status-saver-videos/home")
                ).apply {
                    activity.startActivity(this)
                }
            }

            tvVersionCode.text = "Version:" + PackageInfoCompat.getLongVersionCode(
                activity.packageManager.getPackageInfo(
                    activity.packageName,
                    0
                )
            ) + ".0"


            toolBar.setOnMenuItemClickListener {

                when (it.itemId) {
                    R.id.menu_notification -> {
                        checkNotification(activity)
                    }
                }
                return@setOnMenuItemClickListener true
            }

//            bubbleTabBar.setSelected(R.id.menu_home,true)
            if (intent.getBooleanExtra("isDownloadAvailable", false)) {
                loadFragment(DownloadFragment(), activity)
                binding.chipNavigationBar.setItemSelected(R.id.menu_downloaded, true)
                hideToolBar()
            } else
                loadFragment(HomeFragment(), activity)

//            chipNavigationBar.showBadge(R.id.menu_home, 10)
//            chipNavigationBar.showBadge(R.id.menu_search, 1000)
//            chipNavigationBar.showBadge(R.id.menu_downloaded)
//            chipNavigationBar.showBadge(R.id.menu_bookmark, 100)
            chipNavigationBar.setOnItemSelectedListener {
                when (it) {
                    R.id.menu_home -> {
                        loadFragment(HomeFragment(), activity)
                        showToolBar()
                    }

                    R.id.menu_search -> {

                        toolBar.visibility = View.GONE
                        loadFragment(SearchFragment(), activity)
                        hideToolBar()
                    }

                    R.id.menu_bookmark -> {

                        loadFragment(BookmarkFragment(), activity)
                        hideToolBar()
                    }

                    R.id.menu_downloaded -> {

                        loadFragment(DownloadFragment(), activity)
                        hideToolBar()
                    }
                }
            }

        }


    }


    private fun hideToolBar() {
        binding.toolBar.visibility = View.GONE
    }

    private fun showToolBar() {
        binding.toolBar.visibility = View.VISIBLE
    }


    private fun onBack(context: Activity) {

        var sheetDialog: BottomSheetDialog? = null
        sheetDialog = BottomSheetDialog(context, R.style.BottomSheetStyle)
        val binding: BottomsheetBackpressedBinding =
            BottomsheetBackpressedBinding.inflate(context.layoutInflater)

        binding.apply {
            btnYes.setOnClickListener {
                context.finishAffinity()
            }

            btnNo.setOnClickListener {
                sheetDialog.dismiss()
            }


        }

        sheetDialog.apply {
            setContentView(binding.root)
            setCancelable(true)
            show()
        }
    }

    fun switchBottomNavigationItem(itemId: Int) {
        binding.chipNavigationBar.setItemSelected(itemId, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STORAGE_PERMISSION_CODE && resultCode == Activity.RESULT_OK && data != null) {

            if (isStoragePermissionGranted(activity as Context)) {
                val selectedImageUri: Uri? = data.data
                dismissPermissionDialog(activity as Context)
                Log.i("VISHAY", "ImageUri:${selectedImageUri}")
                selectedImageUri?.let {
                    Constants.saveImageUriToSharedPreferences(this, it)
                }
                setImage()

            } else {
                showPermissionDialog(activity as Context)
            }

        }
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        onBack(activity)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Constants.onRequestPermissionsResult(requestCode, permissions, grantResults, activity)
    }


}
