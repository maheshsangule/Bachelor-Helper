package com.developermaheshapps.bachelorhelper.views.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.ActivityPdfBinding
import com.developermaheshapps.bachelorhelper.utils.CustomScrollHandle
import com.developermaheshapps.bachelorhelper.utils.sharePdf
import com.developermaheshapps.bachelorhelper.views.fragments.PdfToolsFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//fun main()
//{
//    val baseUrl = FirebaseDatabase.getInstance().getReference("baseUrl")
//    Log.i("VISHAY", "main:$baseUrl")
//}
class PdfActivity : AppCompatActivity() {
    val binding by lazy { ActivityPdfBinding.inflate(layoutInflater) }
    lateinit var bookId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBasicViews()
        binding.apply {
            val baseUrl = FirebaseDatabase.getInstance().getReference("baseUrl")


//            val baseUrl = FirebaseDatabase.getInstance().getReference("baseUrl")

            val pdfData = intent.getStringExtra("book_pdf").toString()
            val bookTitle = intent.getStringExtra("bookTitle").toString()
            bookId = intent.getStringExtra("book_id").toString()
            toolBar.title = bookTitle
            toolBar.setNavigationOnClickListener {
                finish()
            }
            toolBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_share -> {

                        sharePdf(this@PdfActivity, bookTitle)

                    }
                }

                return@setOnMenuItemClickListener true
            }


            Log.i("VISHAY", "PdfData:${pdfData}")

            pdfView.fromUri(Uri.parse(pdfData))
                .apply {
                    swipeHorizontal(false)
                        .scrollHandle(CustomScrollHandle(this@PdfActivity))
                        .enableSwipe(true)
                        .pageSnap(true)
                        .autoSpacing(true)
                        .pageFling(true)
                        .load()
                }
        }
    }

    private fun setupBasicViews() {
        binding.mToolsFab.setOnClickListener {
            val toolsBottomSheet = PdfToolsFragment()
            toolsBottomSheet.show(supportFragmentManager, toolsBottomSheet.tag)
        }

    }
}