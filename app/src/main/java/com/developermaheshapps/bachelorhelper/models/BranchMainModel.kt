package com.developermaheshapps.bachelorhelper.models

import java.io.Serializable

data class BranchMainModel(
    var catTitle:String?=null,
    val bookList:ArrayList<BookThumbnailModel>?=null
):Serializable
