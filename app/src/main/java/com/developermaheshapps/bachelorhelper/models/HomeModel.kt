package com.developermaheshapps.bachelorhelper.models

import com.developermaheshapps.bachelorhelper.views.adapters.LAYOUT_HOME
import java.io.Serializable

data class HomeModel(
    val c: String? = null,
    val bl: ArrayList<BookModel>? = null,
    val LAYOUT_TYPE: Int = LAYOUT_HOME
):Serializable
