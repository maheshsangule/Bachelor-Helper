package com.developermaheshsofttechltd.bachelorhelper.models

import com.developermaheshsofttechltd.bachelorhelper.views.adapters.LAYOUT_HOME
import java.io.Serializable

data class HomeModel(
    val c: String? = null,
    val bl: ArrayList<BookModel>? = null,
    val LAYOUT_TYPE: Int = LAYOUT_HOME,
    val isInside:Boolean=false
):Serializable
