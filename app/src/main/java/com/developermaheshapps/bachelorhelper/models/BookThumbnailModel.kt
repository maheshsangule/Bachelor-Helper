package com.developermaheshapps.bachelorhelper.models

import com.developermaheshapps.bachelorhelper.views.adapters.LAYOUT_HOME


data class BookThumbnailModel(
    val image: String,
    val title: String,
    val description: String,
    val authorName: String,
    val bookPdf: String,
    val LAYOUT_TYPE: Int = LAYOUT_HOME
)

//title description bookpdf
