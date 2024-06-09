package com.developermaheshapps.bachelorhelper.utils

sealed class MyResponses<T>(
    val data: T? = null,
    val errorMessage: String? = null,
    val progress: Int = 0
) {
    class Loading<T>(private val mProgress: Int = 0) : MyResponses<T>(progress = mProgress)
    class Error<T>(private val mErrorMsg: String?) : MyResponses<T>(errorMessage = mErrorMsg)
    class Success<T>(private val mData: T?) : MyResponses<T>(data = mData)

}