package com.seongheonson.kakakoimagesearch.bussines.repository

import android.util.Log
import com.seongheonson.kakakoimagesearch.bussines.model.ImageSearch
import com.seongheonson.kakakoimagesearch.bussines.networking.ApiListener
import com.seongheonson.kakakoimagesearch.bussines.networking.KakaoService
import com.seongheonson.kakakoimagesearch.bussines.networking.RetrofitHelper

/**
 * Created by seongheonson on 2018. 10. 12..
 */

class KakaoRepository {

    private var kakaoService = RetrofitHelper().getService(KakaoService::class.java)

    fun search(query: String) {
        RetrofitHelper.request(kakaoService.searchImages(query), object: ApiListener<ImageSearch> {
            override fun onSuccess(t: ImageSearch) {
                Log.d("good", t.meta.toString())
                Log.d("good", t.documents.toString())
            }

            override fun onError(throwable: Throwable) {
                Log.d("good", throwable.message)
            }

            override fun onServerError(errorMessage: String) {
                Log.d("good", errorMessage)
            }

        })
    }

}