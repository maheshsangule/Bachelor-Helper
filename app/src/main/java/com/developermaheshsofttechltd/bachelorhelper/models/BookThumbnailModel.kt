package com.developermaheshsofttechltd.bachelorhelper.models

import com.developermaheshsofttechltd.bachelorhelper.views.adapters.LAYOUT_HOME


data class BookThumbnailModel(
    val image: String,
    val title: String,
    val description: String,
    val authorName: String,
    val bookPdf: String,
    val LAYOUT_TYPE: Int = LAYOUT_HOME
)

//title description bookpdf
