package com.developermaheshapps.bachelorhelper.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.BottmsheetJoinBinding
import com.developermaheshapps.bachelorhelper.databinding.BottomsheetAboutBinding
import com.developermaheshapps.bachelorhelper.databinding.BottomsheetBackpressedBinding
import com.developermaheshapps.bachelorhelper.databinding.BottomsheetHelpBinding
import com.developermaheshapps.bachelorhelper.databinding.BottomsheetNoInternetBinding
import com.developermaheshapps.bachelorhelper.databinding.BottomsheetNotificationBinding
import com.developermaheshapps.bachelorhelper.databinding.BottomsheetUserProfileBinding
import com.developermaheshapps.bachelorhelper.models.DevAboutModel
import com.developermaheshapps.bachelorhelper.models.DevJoinModel
import com.developermaheshapps.bachelorhelper.views.activities.HomeActivity
import com.developermaheshapps.bachelorhelper.views.adapters.DevAboutAdapter
import com.developermaheshapps.bachelorhelper.views.adapters.DevJoinAdapter
import com.developermaheshapps.bachelorhelper.views.fragments.HomeFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun getFileExtension(fileName: String): String {
    val lastDotIndex = fileName.lastIndexOf(".")
    if (lastDotIndex >= 0 && lastDotIndex < fileName.length - 1) {
        return fileName.substring(lastDotIndex + 1)
    }
    return ""
}

@SuppressLint("StaticFieldLeak")
object Constants {

    const val STORAGE_PERMISSION_CODE = 101
    private var activity: Activity? = null
    private const val IMAGE_URI_KEY = "image_uri_key"
    private var sheetDialog: BottomSheetDialog? = null
    private lateinit var selectImage: ActivityResultLauncher<String>
    private var dialog: AlertDialog? = null
    private var isPermissionDialogShown = false
    private lateinit var binding: BottomsheetUserProfileBinding

    @SuppressLint("SetTextI18n")
    fun devData(context: Activity, title: String) {
        val listJoin = ArrayList<DevJoinModel>()
        val listAbout = ArrayList<DevAboutModel>()

        val adapterJoin = DevJoinAdapter(listJoin, context)
        val adapterAbout = DevAboutAdapter(listAbout, context)

        sheetDialog = BottomSheetDialog(context, R.style.BottomSheetStyle)

        val bindingJoin: BottmsheetJoinBinding =
            BottmsheetJoinBinding.inflate(context.layoutInflater)
        val bindingHelp: BottomsheetHelpBinding =
            BottomsheetHelpBinding.inflate(context.layoutInflater)
        val bindingAbout: BottomsheetAboutBinding =
            BottomsheetAboutBinding.inflate(context.layoutInflater)

        val imageList = listOf<Int>(
            R.drawable.ic_instagram,
            R.drawable.ic_linkedin,
            R.drawable.ic_youtube,
            R.drawable.ic_email,
            R.drawable.ic_whatsapp,
            R.drawable.ic_facebook
        )
        val titleList = listOf<String>(
            "Instagram",
            "LinkedIn",
            "Youtube",
            "Email",
            "WhatsApp",
            "Facebook"
        )
        val subTitleList = listOf<String>(
            "@developer_mahesh",
            "mahesh-sangule",
            "@developer-mahesh",
            "@developermaheshhelp@gmail.com",
            "@developer_mahesh",
            "@developer_mahesh"
        )

        bindingJoin.apply {
            rvJoin.adapter = adapterJoin
            SpringScrollHelper().attachToRecyclerView(rvJoin)

            for (i in imageList.indices) {
                listJoin.apply {
                    add(DevJoinModel(imageList[i], titleList[i], subTitleList[i]))
                }
            }

        }

        bindingHelp.apply {
            val userName = SharedPrefUtils.getPrefString("name", "").toString()
            etName.setText(userName.toString())
            mbtnSubmit.setOnClickListener {

                if (etName.text.isEmpty() || etMessage.text.isEmpty())
                    Toast.makeText(context, "Please enter all Data", Toast.LENGTH_SHORT).show()
                else
                    linkOpen(
                        context,
                        context.getString(R.string.email),
                        etName.text.toString(),
                        etMessage.text.toString()
                    )
            }
        }

        bindingAbout.apply {
            tvVersionCode.text = "Version:" + PackageInfoCompat.getLongVersionCode(
                context.packageManager.getPackageInfo(
                    context.packageName,
                    0
                )
            ) + ".0"
            rvAbout.adapter = adapterAbout

            llPrivacy.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://sites.google.com/view/status-saver-videos/home")
                ).apply {
                    context.startActivity(this)
                }
            }

            llShare.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                    putExtra(                        Intent.EXTRA_TEXT,
                        "\uD83C\uDF93 Unlock Your Bachelor Journey with Bachelor Helper!" +
                                " \uD83D\uDCDA Dive into a vast library of free books covering all" +
                                " bachelor streams, from self-improvement to career advancement. Find" +
                                "quick meal recipes, relationship advice, and more tailored to your " +
                                "bachelor lifestyle. Access exclusive features for organizing your " +
                                "schedule and discovering the hottest hangout spots. Connect with a " +
                                "thriving community of like-minded bachelors to share tips and experiences." +
                                " Download Bachelor Helper now to embark on a journey of self-discovery," +
                                " growth, and limitless" +
                                " possibilities! :${SharedPrefUtils.getPrefString("driveApkUrl","")}"
                    )
                    context.startActivity(this)
                }
            }

            llRate.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
                ).apply {
                    context.startActivity(this)
                }
            }
            for (i in imageList.indices) {
                listAbout.apply {
                    add(DevAboutModel(imageList[i], titleList[i]))
                }
            }
        }

        sheetDialog?.apply {
            when (title) {
                "Join" -> {
                    setContentView(bindingJoin.root)
                }

                "Help" -> {
                    setContentView(bindingHelp.root)
                }

                "About" -> {
                    setContentView(bindingAbout.root)

                }
            }
            setCancelable(true)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            show()
        }

    }

    fun checkNotification(context: Activity)
    {
        sheetDialog = BottomSheetDialog(context, R.style.BottomSheetStyle)

        val binding: BottomsheetNotificationBinding =
            BottomsheetNotificationBinding.inflate(context.layoutInflater)

        binding.apply {
            btnClose.setOnClickListener {
                sheetDialog!!.dismiss()
            }
        }
        sheetDialog?.apply {
            setContentView(binding.root)
            setCancelable(true)
            show()
        }

    }

    @SuppressLint("ResourceAsColor")
    fun checkInternet(context: Activity, isSplash: Boolean = false) {
        sheetDialog = BottomSheetDialog(context, R.style.BottomSheetStyle)

        val binding: BottomsheetNoInternetBinding =
            BottomsheetNoInternetBinding.inflate(context.layoutInflater)
        val retrievedList = SharedPreferencesHelper.getStringList(context).toSet()
        binding.apply {
            if (!isConnected(context)) {

                if (retrievedList.isEmpty()) {
                    btnViewDownloads.isEnabled = false
                    btnViewDownloads.setTextColor(R.color.black)
                }

                btnViewDownloads.setOnClickListener {
                    Intent().apply {
                        setClass(context, HomeActivity::class.java)
                        putExtra("isDownloadAvailable", true)
                        context.startActivity(this)
                        context.finish()
                    }
                }
                btnRetry.setOnClickListener {
                    if (isSplash) {
                        sheetDialog!!.dismiss()
                        context.finish()
                    } else
                    {
                        sheetDialog!!.dismiss()
                        checkInternet(context)
                    }

                }


            }
            else{
                btnRetry.setOnClickListener {
                        sheetDialog!!.dismiss()
                }
                if (retrievedList.isEmpty()) {
                    btnViewDownloads.isEnabled = false
                    btnViewDownloads.setTextColor(R.color.black)
                }

                btnViewDownloads.setOnClickListener {
                    Intent().apply {
                        setClass(context, HomeActivity::class.java)
                        putExtra("isDownloadAvailable", true)
                        context.startActivity(this)
                        context.finish()
                    }
                }
            }
        }
        sheetDialog?.apply {
            setContentView(binding.root)
            setCancelable(false)
            show()
        }
    }

    fun isConnected(context: Activity): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnectedOrConnecting
    }

    @SuppressLint("SetTextI18n")
    fun userProfile(context: Activity, name: String = "") {
        SharedPrefUtils.init(context as Context)
        val sheetDialog: BottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetStyle)

        binding =
            BottomsheetUserProfileBinding.inflate(context.layoutInflater)
        sheetDialog.setContentView(binding.root)


        binding.apply {

            etUserName.clearFocus()
            etUserName.clearComposingText()
            setImage()


            ivUserProfile.setOnClickListener {
                if (!isStoragePermissionGranted(context)) {
                    Log.i("VISHAY", "Clicked permission not granted requesting for permission")

                    requestStoragePermission(context)

                } else {
                    Log.i("VISHAY", "Permission already granted opening gallery")
                    openGallery(context as Activity)

                }
            }


            var username = SharedPrefUtils.getPrefString("name", "")
            tvName.text = username.toString()
            if (username != null) {
                if (username.isNotEmpty() && username != "Guestxxxx9164") {
                    mbtnSubmit.text = "Update"
                    etUserName.setText(username.toString())
                }

            }

            mbtnSubmit.setOnClickListener {
                if (etUserName.text.trim().isEmpty() && etUserName.text.isBlank()) {
                    etUserName.text.clear()
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                } else {

                    SharedPrefUtils.putPrefString("name", etUserName.text.toString())
                    username = SharedPrefUtils.getPrefString("name", "").toString().trim()
                    tvName.text = username
                    username = SharedPrefUtils.getPrefString("name", "")
                    if (tvName.text.isNotEmpty()) {
                        if (!SharedPrefUtils.getPrefBoolean("isLogin", false)) {
                            Handler(Looper.myLooper()!!).postDelayed({
//                                HomeFragment().updateUserDetails()
                                sheetDialog.dismiss()
                            }, 300)
                        }

                        SharedPrefUtils.putPrefBoolean("isLogin", true)

                        mbtnSubmit.text = "Update"
                    } else
                        mbtnSubmit.text = "Submit"

                }
            }

            sheetDialog.apply {

                if (!SharedPrefUtils.getPrefBoolean("isLogin", false))
                    setCancelable(false)
                else {

                    setCancelable(true)
                }


                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                show()
            }
        }
    }

    fun linkOpen(context: Context, url: String, etName: String = "", etMessage: String = "") {
        if (url.startsWith("d")) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf<String>(context.getString(R.string.email))
            )
            if (etName.isNotEmpty() && etMessage.isNotEmpty()) {
                intent.putExtra(Intent.EXTRA_SUBJECT, etName)
                intent.putExtra(Intent.EXTRA_TEXT, etMessage)
            } else {
                intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
                intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body))
            }


            try {
                context.startActivity(
                    Intent.createChooser(
                        intent,
                        context.getString(R.string.app_name)
                    )
                )
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, "No mail app found!!!", Toast.LENGTH_SHORT)
                    .show()
            } catch (ex: Exception) {
                Toast.makeText(context, "Unexpected Error!!!", Toast.LENGTH_SHORT).show()
            }
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }


    fun setImage() {
        val userProfile = SharedPrefUtils.getPrefString("profilePic", "").toString()
        if (userProfile.isNotEmpty()) {
            binding.ivUserProfile.loadUserProfile(userProfile)
        }
    }

    private fun openGallery(context: Activity) {
        Log.i("VISHAY", "Opening Gallery")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, STORAGE_PERMISSION_CODE)
    }


    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        activity: Activity
    ) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("VISHAY", "Request Permission dismisspermissionDialog")
                SharedPrefUtils.putPrefBoolean("isProfilePicPermissionGranted", true)
                dismissPermissionDialog(activity as Context)
                openGallery(activity)
            } else {
                Log.i("VISHAY", "Request Permission ShowpermissionDialog")
                showPermissionDialog(activity as Context)
            }
        }
    }

    fun saveImageUriToSharedPreferences(context: Context, uri: Uri) {
        SharedPrefUtils.putPrefString("profilePic", uri.toString())
    }

    fun showPermissionDialog(context: Context) {
        Log.i("VISHAY", "Showing Permission Dialog")

        if (!SharedPrefUtils.getPrefBoolean("isProfilePicPermissionGranted", false)) {
            initializePermissionDialog(context)
            dialog?.show()
            isPermissionDialogShown = true
        }
    }

    private fun initializePermissionDialog(context: Context) {
        Log.i("VISHAY", "Initializing Dialog")
        val alertDialog =
            MaterialAlertDialogBuilder(context, R.style.CustomAlertDialogStyle)
                .apply {


                    setTitle("Permission Required")
                    setBackground(ContextCompat.getDrawable(context, R.drawable.degree_back))
                    setMessage("This app really need to use this permission. Do you want to allow it")
                    setPositiveButton("OK") { _, _ ->
                        openAppSettings(context as Activity)

                    }
                    setCancelable(true)
                }
                .create()

        dialog = alertDialog


    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun dismissPermissionDialog(context: Context) {
        Log.i("VISHAY", "dismissing Dialog")

        dialog?.dismiss()
        isPermissionDialogShown = false
    }

    private fun openAppSettings(context: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivityForResult(intent, STORAGE_PERMISSION_CODE)
    }

    fun isStoragePermissionGranted(context: Context): Boolean {

        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            Log.i("VISHAY", "checking permission in less than android 10 versions")
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            Log.i("VISHAY", "checking permission in greater than android 10 versions")

            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun requestStoragePermission(context: Activity) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
            )
            Log.i("VISHAY", "Requesting permission in less than android 10 versions")
        } else {

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES), STORAGE_PERMISSION_CODE
            )
            Log.i("VISHAY", "Requesting permission in greater than android 10 versions")
        }

    }


}
