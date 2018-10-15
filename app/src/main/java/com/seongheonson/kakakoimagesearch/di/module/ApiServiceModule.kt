package com.seongheonson.kakakoimagesearch.di.module

import com.seongheonson.kakakoimagesearch.api.*
import com.seongheonson.kakakoimagesearch.common.API_REST_KEY
import com.seongheonson.kakakoimagesearch.common.API_REST_URL
import com.seongheonson.kakakoimagesearch.common.NAMED_REST_API_KEY
import com.seongheonson.kakakoimagesearch.common.NAMED_REST_API_URL
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiServiceModule {

    @Provides
    @Singleton
    @Named(NAMED_REST_API_URL)
    fun provideRestApiUrl(): String = API_REST_URL

    @Provides
    @Singleton
    @Named(NAMED_REST_API_KEY)
    fun provideRestApiKey(): String = API_REST_KEY

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): KakaoService {
        return retrofit.create(KakaoService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named(NAMED_REST_API_URL) baseUrl: String, client: OkHttpClient,
                        converterFactory: Converter.Factory, callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(netWorkInterceptor: HttpLoggingInterceptor, interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(netWorkInterceptor)
                .addInterceptor(interceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun getInterceptor(@Named(NAMED_REST_API_KEY) apiKey: String?): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
            if (apiKey != null) requestBuilder.header("Authorization", "KakaoAK $apiKey")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideRxJavaCallAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }
}
