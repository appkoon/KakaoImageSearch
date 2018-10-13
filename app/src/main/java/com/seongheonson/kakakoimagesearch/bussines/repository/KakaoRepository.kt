package com.seongheonson.kakakoimagesearch.bussines.repository

import com.seongheonson.kakakoimagesearch.bussines.model.Response
import com.seongheonson.kakakoimagesearch.bussines.networking.KakaoService
import com.seongheonson.kakakoimagesearch.bussines.networking.RetrofitHelper
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by seongheonson on 2018. 10. 12..
 */

class KakaoRepository {

    private var kakaoService = RetrofitHelper().getService(KakaoService::class.java)

    fun search(query: String, page: Int, size: Int) : Single<Response> = kakaoService.searchImages(query, page, size).subscribeOn(Schedulers.io())


    /*
    RetrofitHelper.request(kakaoService.searchImages(query), object: ApiListener<Response> {
            override fun onSuccess(t: Response) {
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
    * */
}