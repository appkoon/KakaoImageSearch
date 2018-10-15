package com.seongheonson.kakakoimagesearch.di.module

import com.seongheonson.kakakoimagesearch.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by seongheonson on 2018. 10. 12..
 */
@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}