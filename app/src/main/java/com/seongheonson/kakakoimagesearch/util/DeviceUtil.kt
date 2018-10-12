package com.seongheonson.kakakoimagesearch.util

import android.content.Context


/**
 * Created by seongheonson on 2018. 10. 12..
 */

class DeviceUtil(context: Context) {

    var display = context.resources.displayMetrics
    var width = display.widthPixels
    var height = display.heightPixels

}