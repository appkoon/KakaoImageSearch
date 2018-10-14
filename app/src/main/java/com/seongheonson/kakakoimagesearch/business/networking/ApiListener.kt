package com.seongheonson.kakakoimagesearch.business.networking

/**
 * Created by seongheonson on 2018. 10. 12..
 */

interface ApiListener<T> {
    fun onSuccess(response: T)
    fun onError(throwable: Throwable)
    fun onServerError(errorMessage: String)
}