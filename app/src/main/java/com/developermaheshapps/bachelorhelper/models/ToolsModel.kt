package com.developermaheshapps.bachelorhelper.models

import androidx.annotation.DrawableRes
import com.developermaheshapps.bachelorhelper.enums.ToolsType

data class ToolsModel(
    val title: String,
    @DrawableRes
    val image: Int,
    val type: ToolsType
)
