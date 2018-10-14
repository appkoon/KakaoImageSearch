package com.seongheonson.kakakoimagesearch.binding

import android.databinding.BindingAdapter
import android.util.Log
import android.view.View

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}