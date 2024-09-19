package com.developermaheshsofttechltd.bachelorhelper.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.developermaheshsofttechltd.bachelorhelper.database.AppDatabase
import com.developermaheshsofttechltd.bachelorhelper.entities.BookmarksEntity
import com.developermaheshsofttechltd.bachelorhelper.entities.NotesEntity
import com.developermaheshsofttechltd.bachelorhelper.views.activities.PdfActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("ALL")
class ToolsViewModel(private val pdfActivity: PdfActivity) : ViewModel() {

    private var nightMode = false
    val database = AppDatabase.getDatabase(pdfActivity)

    fun toggleNightMode() {
        nightMode = !nightMode
        pdfActivity.binding.pdfView.setNightMode(nightMode)

    }

    fun jumpToPage(pageNo: Int) {
        pdfActivity.binding.pdfView.jumpTo(pageNo, true)
    }

    fun addBookmark() {
        CoroutineScope(Dispatchers.IO).launch {
            val entity =
                BookmarksEntity(0, pdfActivity.bookId, pdfActivity.binding.pdfView.currentPage)
            database?.bookmarksDao()?.addBookmark(entity)
        }
    }

    fun addNote(note: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = NotesEntity(0, pdfActivity.bookId, note)
            database?.notesDao()?.addNote(entity)

        }
    }


}



















