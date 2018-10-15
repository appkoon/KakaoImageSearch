package com.seongheonson.kakakoimagesearch.repository

import com.seongheonson.kakakoimagesearch.vo.ImageSearch
import com.seongheonson.kakakoimagesearch.api.KakaoService
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by seongheonson on 2018. 10. 12..
 */
@Singleton
class KakaoRepository @Inject constructor(private val kakaoService: KakaoService) {

    fun search(query: String, page: Int, size: Int) : Single<Response<ImageSearch>> =
            kakaoService.searchImages(query, page, size)

}