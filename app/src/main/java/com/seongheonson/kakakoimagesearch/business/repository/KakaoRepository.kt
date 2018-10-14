package com.seongheonson.kakakoimagesearch.business.repository

import com.seongheonson.kakakoimagesearch.business.model.ImageSearch
import com.seongheonson.kakakoimagesearch.business.networking.KakaoService
import com.seongheonson.kakakoimagesearch.business.networking.RetrofitHelper
import io.reactivex.Single
import retrofit2.Response

/**
 * Created by seongheonson on 2018. 10. 12..
 */

class KakaoRepository {

    private var kakaoService = RetrofitHelper().getService(KakaoService::class.java)

    fun search(query: String, page: Int, size: Int) : Single<Response<ImageSearch>> = kakaoService.searchImages(query, page, size)


   /* RetrofitHelper.request(kakaoService.searchImages(query), object: ApiListener<ImageSearch> {
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

    })*/

}