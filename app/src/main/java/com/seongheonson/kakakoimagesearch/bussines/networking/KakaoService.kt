package com.seongheonson.kakakoimagesearch.bussines.networking

import com.seongheonson.kakakoimagesearch.bussines.model.ImageSearch
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by seongheonson on 2018. 10. 12..
 */

interface KakaoService {

    @GET("search/image")
    fun searchImages(@Query("query") query: String): Single<Response<ImageSearch>>

}