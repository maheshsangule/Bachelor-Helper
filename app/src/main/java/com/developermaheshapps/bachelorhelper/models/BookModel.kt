package com.developermaheshapps.bachelorhelper.models

import java.io.Serializable

data class BookModel(
    val i: String = "".trim(),
    val t: String = "".trim(),
    val d: String = "",
    val a: String = "",
    val b: String = "".trim()
) : Serializable
