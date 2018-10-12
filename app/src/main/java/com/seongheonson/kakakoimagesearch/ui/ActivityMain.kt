package com.seongheonson.kakakoimagesearch.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.ui.search.FragmentSearch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentSearch())
                .commitNow()
    }


}
