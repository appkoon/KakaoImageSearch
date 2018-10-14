package com.seongheonson.kakakoimagesearch.di.module

import android.app.Application
import android.content.Context
import com.seongheonson.kakakoimagesearch.business.networking.KakaoService
import com.seongheonson.kakakoimagesearch.business.networking.RetrofitHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by seongheonson on 2018. 10. 12..
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideGithubService(): KakaoService {
        return RetrofitHelper().getKakaoService()
    }

}