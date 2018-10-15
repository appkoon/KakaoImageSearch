package com.seongheonson.kakakoimagesearch.business.repository

import com.seongheonson.kakakoimagesearch.business.model.ImageSearch
import com.seongheonson.kakakoimagesearch.business.networking.KakaoService
import com.seongheonson.kakakoimagesearch.business.networking.RetrofitHelper
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by seongheonson on 2018. 10. 12..
 */
@Singleton
class KakaoRepository @Inject constructor(private val kakaoService: KakaoService) {

//    private var kakaoService = RetrofitHelper().getService(KakaoService::class.java)

    fun search(query: String, page: Int, size: Int) : Single<Response<ImageSearch>> = kakaoService.searchImages(query, page, size)

}