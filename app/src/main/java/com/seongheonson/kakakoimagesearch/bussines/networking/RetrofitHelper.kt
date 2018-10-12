package com.seongheonson.kakakoimagesearch.bussines.networking

import android.annotation.SuppressLint
import android.app.PendingIntent.getService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by seongheonson on 2018. 10. 12..
 */



class RetrofitHelper {

    val BASE_URL = "https://dapi.kakao.com/v2/"
    val API_KEY = "40d6fa725be71d93c7f0754921a1ccaa"

    private val retrofit: Retrofit.Builder = Retrofit.Builder()
                                            .baseUrl(BASE_URL)
                                            .client(createDefaultClient())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


    private fun createDefaultClient() : OkHttpClient {
        return OkHttpClient().newBuilder()
                             .readTimeout(1, TimeUnit.MINUTES)
                             .connectTimeout(1, TimeUnit.MINUTES)
                             .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                             .addInterceptor(getInterceptor(API_KEY)).build()
    }


    private fun getInterceptor(accessToken: String?): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                                                        .header("Accept", "application/json")
                                                        .method(original.method(), original.body())

            if (accessToken != null) requestBuilder.header("Authorization", "KakaoAK $accessToken")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }


    fun <T> getService(tService: Class<T>): T =  retrofit.build().create(tService)

    fun getKakaoService() : KakaoService = getService(KakaoService::class.java)

    companion object {

        @SuppressLint("CheckResult")
        fun <T> request(single: Single<Response<T>>, listener: ApiListener<T>) {
            single.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ tResponse: Response<T> ->
                                    if (tResponse.isSuccessful) listener.onSuccess(tResponse.body()!!)
                                    else listener.onServerError(tResponse.errorBody().toString())
                                }, listener::onError)
        }
    }
}
