package com.seongheonson.kakakoimagesearch.di

import android.app.Application
import com.seongheonson.kakakoimagesearch.App
import com.seongheonson.kakakoimagesearch.di.module.ApiServiceModule
import com.seongheonson.kakakoimagesearch.di.module.AppModule
import com.seongheonson.kakakoimagesearch.di.module.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    MainActivityModule::class,
    ApiServiceModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}