package com.developermaheshsofttechltd.bachelorhelper.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.developermaheshsofttechltd.bachelorhelper.R
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutDisclaimerBinding
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutUpdateAvailableBinding
import com.developermaheshsofttechltd.bachelorhelper.repository.UpdateRepo
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.UpdateViewModel
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories.UpdateViewModelFactory
import java.io.File

class UpdateManager(private val context: Activity) {
    init {
        SharedPrefUtils.init(context)
    }

    //        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefString("baseUrl", "")}")
//        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefString("apkUrl", "")}")
//        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefString("version", "")}")
//        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefBoolean("isCompulsory", false)}")
    private lateinit var dialog: Dialog
    private val dialogBinding = LayoutUpdateAvailableBinding.inflate(LayoutInflater.from(context))
    private val apkUrl: String = SharedPrefUtils.getPrefString("apkUrl", "").toString()
    private val version: String = SharedPrefUtils.getPrefString("version", "").toString()
    private val isCompulsory: Boolean = SharedPrefUtils.getPrefBoolean("isCompulsory", false)
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    private var isDownloaded: Boolean = false
    val apkFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
        "Bachelor Helper.apk"
    )
    //    private val apkDriveUrl: String = SharedPrefUtils.getPrefString("apkDriveUrl", "").toString()
    private var downloadId: Long = -1


    private val repo = UpdateRepo(context)

    private val viewModel: UpdateViewModel by lazy {
        val factory = UpdateViewModelFactory(repo)
        ViewModelProvider(context as ViewModelStoreOwner, factory).get(UpdateViewModel::class.java)
    }


    fun showDisclaimerDialog() {
        val dialogBinding = LayoutDisclaimerBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(0))
        }

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun checkForUpdates() {
//        SharedPrefUtils.init(context)
        val isUpdateAvailable = isUpdateAvailable(context)
        if (isUpdateAvailable) {
            SharedPrefUtils.putPrefBoolean("isUpdateAvailable",true)
            showUpdateDialog()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun showUpdateDialog() {


        dialog = Dialog(context as Activity).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialogBinding.apply {
            if (!isCompulsory) {
                btnCancel.isEnabled = false
                btnCancel.setTextColor(R.color.black)
            }
            btnUpdate.setOnClickListener {

                if (isDownloaded)
                    viewModel.updateApp()
                else {
                    viewModel.updateApp()
                    backgroundTask()
                }
            }

            btnCancel.setOnClickListener {
                if (!SharedPrefUtils.getPrefBoolean("isLogin", false))
                    Constants.userProfile(context)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun backgroundTask() {
        try {
            viewModel.downloadLiveData.observe(context as LifecycleOwner)
            {

//            Toast.makeText(context, "Coming", Toast.LENGTH_SHORT).show()
                when (it) {
                    is MyResponses.Error -> {
                        isDownloaded = true
                        dialogBinding.btnUpdate.isEnabled = true
                        dialogBinding.btnUpdate.text = "Retry"
                    }

                    is MyResponses.Loading -> {
                        dialogBinding.btnUpdate.text = "${it.progress}%"
                        dialogBinding.btnUpdate.isEnabled = false
                        Log.d("VISHAY", "checkDownloadStatus: ${it.progress}%")

                    }

                    is MyResponses.Success -> {

                        isDownloaded = true
                        dialogBinding.btnUpdate.isEnabled = true
                        dialogBinding.btnUpdate.text = "Install"
                        installApk()
                        viewModel.downloadLiveData.removeObservers(context as LifecycleOwner)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun installApk() {


        try {
            if (!apkFile.exists()) {
                Log.e("VISHAY", "APK file does not exist: ${apkFile.absolutePath}")
                return
            } else {
                val apkUri =
                    FileProvider.getUriForFile(context, "${context.packageName}.provider", apkFile)

                val installIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(apkUri, "application/vnd.android.package-archive")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }

                // Verify that there are applications available to handle this intent
                val activities = context.packageManager.queryIntentActivities(installIntent, 0)
                val isIntentSafe = activities.isNotEmpty()

                if (isIntentSafe) {
                    context.startActivity(installIntent)
//                    apkFile.delete()
                } else {
                    Log.e("VISHAY", "No application available to handle the intent")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("VISHAY", "Error installing APK: ${e.localizedMessage}")
        }
    }


    private fun isUpdateAvailable(context: Context): Boolean {
        val currentVersionCode = getCurrentVersionCode(context)
        val latestVersionCode =
            getLatestVersionCodeFromServer() // Replace with your logic to retrieve latest version code

//        Log.d("VISHAY", "isUpdateAvailable:$currentVersionCode ")
//        Log.d("VISHAY", "isUpdateAvailable: $latestVersionCode")
//        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefString("baseUrl", "")}")
//        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefString("apkUrl", "")}")
//        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefString("version", "")}")
//        Log.d("VISHAY", "isUpdateAvailable: ${SharedPrefUtils.getPrefBoolean("isCompulsory", false)}")

        return latestVersionCode > currentVersionCode
    }

    private fun getCurrentVersionCode(context: Context): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            -1
        }
    }

    private fun getLatestVersionCodeFromServer(): Long {
        return version.toLong()
    }


}
