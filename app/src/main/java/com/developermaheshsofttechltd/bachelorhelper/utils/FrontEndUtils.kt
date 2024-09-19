package com.developermaheshsofttechltd.bachelorhelper.utils

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import java.io.File

const val FOLDER_PATH = "Bachelor Helper"
fun ImageView.loadOnline(imageUrl: String) {
    val baseUrl = SharedPrefUtils.getPrefString("baseUrl", "").toString()

//    Glide.with(this.context)
//        .load(R.drawable.placeholder_animation) // Placeholder animation resource
//        .into(this)
    Glide.with(this.context)
        .load(baseUrl + imageUrl)
//        .apply(RequestOptions().placeholder(androidx.appcompat.R.drawable.abc_btn_check_material_anim))
        .fitCenter()
        .transition(withCrossFade())
        .thumbnail(0.1f)
        .into(this)
}

fun ImageView.loadUserProfile(imageUrl: String) {
    Log.d(
        "VISHAY",
        "frontendutils:${SharedPrefUtils.getPrefString("profilePic", "").toString().toUri()} "
    )
    Glide.with(context)
        .load(imageUrl.toUri())
        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
        .into(this)
}

fun sharePdf(context: Activity, pdfTitle: String) {
    try {
        val externalDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destFile = File(
            externalDir,
            "/$FOLDER_PATH/$pdfTitle.pdf"
        )
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", destFile)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/pdf"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "Share PDF using:"))
    } catch (e: Exception) {
        e.localizedMessage?.let {
            Log.d("Share", it.toString())
        }
    }
}


