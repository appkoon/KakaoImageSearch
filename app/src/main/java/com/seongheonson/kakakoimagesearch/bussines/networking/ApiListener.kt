package com.seongheonson.kakakoimagesearch.bussines.networking

/**
 * Created by seongheonson on 2018. 10. 12..
 */

interface ApiListener<T> {
    fun onSuccess(t: T)
    fun onError(throwable: Throwable)
    fun onServerError(errorMessage: String)
}