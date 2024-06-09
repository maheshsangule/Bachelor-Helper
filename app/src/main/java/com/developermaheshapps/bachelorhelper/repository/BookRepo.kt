package com.developermaheshapps.bachelorhelper.repository

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.developermaheshapps.bachelorhelper.utils.FOLDER_PATH
import com.developermaheshapps.bachelorhelper.utils.MyResponses
import kotlinx.coroutines.delay
import java.io.File

class BookRepo(private val context: Context) {
    private val downloadLd = MutableLiveData<MyResponses<DownloadModel>>()
    val downloadLiveData get() = downloadLd
    private val TAG = "BookRepo"

    @SuppressLint("Range")
    suspend fun downloadPdf(url: String, fileName: String) {
//        val downloadDir =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "/$FOLDER_PATH/$fileName"
        )
        Log.i(TAG, "downloadPdf: ${file.absolutePath}")

        if (file.exists()) {
            val model = DownloadModel(
                progress = 100,
                isDownloaded = true,
                downloadId = -1,
                filePath = file.toURI().toString()
            )
            downloadLiveData.postValue(MyResponses.Success(model))
            Log.i(TAG, "downloadPdf: File Already Exists")
            return
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadRequest = DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            setTitle(fileName)
            setDescription("Downloading Book")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setAllowedOverRoaming(false)
            setAllowedOverMetered(true)
            setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                "$FOLDER_PATH/$fileName"
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
                            downloadLiveData.postValue(MyResponses.Loading(progress))
                            Log.d("VISHAY", "checkDownloadStatus: ${progress}%")
                        }
                    }

                    DownloadManager.STATUS_FAILED -> {
                        val reason =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                        isDownloaded = true
                        downloadLiveData.postValue(MyResponses.Error("Failed to Download $fileName.\nReason $reason"))
                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        isDownloaded = true
                        val filePath =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                        val model = DownloadModel(
                            progress = progress,
                            isDownloaded = isDownloaded,
                            downloadId = downloadId,
                            filePath = filePath
                        )
                        downloadLiveData.postValue(MyResponses.Success(model))
                    }
                }
            }
            cursor.close()
            delay(100) // Delay for 1 second before querying download status again
        }
    }

    data class DownloadModel(
        var progress: Int = 0, var isDownloaded: Boolean, var downloadId: Long, var filePath: String
    )
}

