package com.developermaheshsofttechltd.bachelorhelper.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.developermaheshsofttechltd.bachelorhelper.utils.MyResponses
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshsofttechltd.bachelorhelper.utils.UpdateManager
import kotlinx.coroutines.delay
import java.io.File

class UpdateRepo(private val context: Context) {
    private val updateLD = MutableLiveData<MyResponses<String>>()
    val updateLiveData get() = updateLD
    private val TAG = "UpdateRepo"
    private val apkUrl: String = SharedPrefUtils.getPrefString("apkUrl", "").toString()

    init {
        SharedPrefUtils.init(context)
    }

    @SuppressLint("Range")
    suspend fun updateApp() {
        val apkFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "Bachelor Helper.apk"
        )

        if (apkFile.exists()) {
            UpdateManager(context as Activity).installApk()
        }
        else
        {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadRequest = DownloadManager.Request(Uri.parse(apkUrl)).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                setTitle("Bachelor Helper")
                setDescription("Downloading Update")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

                setAllowedOverRoaming(false)
                setAllowedOverMetered(true)
                setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    "Bachelor Helper.apk"
                )
            }

            val downloadId = downloadManager.enqueue(downloadRequest)
            var isDownloaded = false
            var progress = 0

            while (!isDownloaded) {
                val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    when (status) {
                        DownloadManager.STATUS_RUNNING -> {
                            val totalSize =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                            if (totalSize > 0) {
                                val downloadBytesSize =
                                    cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                                progress = ((downloadBytesSize * 100) / totalSize).toInt()
                                updateLiveData.postValue(MyResponses.Loading(progress))
//                            Log.d("VISHAY", "checkDownloadStatus: ${progress}%")
                            }
                        }

                        DownloadManager.STATUS_FAILED -> {
                            val reason =
                                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                            isDownloaded = true
                            updateLiveData.postValue(MyResponses.Error("Failed to Download.\nReason $reason"))
                        }

                        DownloadManager.STATUS_SUCCESSFUL -> {
                            updateLiveData.postValue(MyResponses.Success("Completed"))
//                            UpdateManager(context as Activity).installApk()
                        }
                    }
                }
                cursor.close()
                delay(100)
            }
        }

    }
}