package com.seongheonson.kakakoimagesearch.business.networking

import com.seongheonson.kakakoimagesearch.business.model.ImageSearch
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by seongheonson on 2018. 10. 12..
 */

interface KakaoService {

    @GET("search/image")
    fun searchImages(@Query("query") query: String, @Query("page") page: Int, @Query("size") size: Int): Single<Response<ImageSearch>>

}