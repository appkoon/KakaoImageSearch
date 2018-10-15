package com.seongheonson.kakakoimagesearch.api

/**
 * Created by seongheonson on 2018. 10. 12..
 */

interface ApiResponse<T> {
    fun onSuccess(response: T)
    fun onError(throwable: Throwable)
    fun onServerError(errorMessage: String)
}