package com.developermaheshsofttechltd.bachelorhelper.models

import androidx.annotation.DrawableRes
import com.developermaheshsofttechltd.bachelorhelper.enums.ToolsType

data class ToolsModel(
    val title: String,
    @DrawableRes
    val image: Int,
    val type: ToolsType
)
