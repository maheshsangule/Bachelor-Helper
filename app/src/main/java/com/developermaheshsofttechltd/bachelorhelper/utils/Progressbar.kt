package com.developermaheshsofttechltd.bachelorhelper.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutProgressBinding

object Progressbar {
    private lateinit var progressDialog: Dialog


    private fun showProgressBar(context: Context): Dialog {
        val dialogBinding = LayoutProgressBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        return dialog
    }


    fun loader(context: Context, state: Boolean = true) {
        if (!::progressDialog.isInitialized) {
            progressDialog = showProgressBar(context)
        }

        if (state) {
            progressDialog.show()
        } else {
            if (::progressDialog.isInitialized && progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }
    }


}
