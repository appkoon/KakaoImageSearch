package com.seongheonson.kakakoimagesearch

import com.seongheonson.kakakoimagesearch.business.networking.KakaoService
import com.seongheonson.kakakoimagesearch.business.networking.RetrofitHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.schedulers.Schedulers

/**
 * Created by seongheonson on 2018. 10. 12..
 */
@RunWith(JUnit4::class)
class RetrofitTest {

    lateinit var kakaoService: KakaoService

    @Before
    fun beforeTest() {
        kakaoService = RetrofitHelper().getService(KakaoService::class.java)
    }

    @Test
    fun getImage() {
        val resultObservable = kakaoService.searchImages("강아지")
        resultObservable.subscribeOn(Schedulers.io()).subscribe { response ->
            if(response.isSuccessful) {
                print(response.body())
            } else {
                print(response.errorBody().toString())
            }
        }
    }

}