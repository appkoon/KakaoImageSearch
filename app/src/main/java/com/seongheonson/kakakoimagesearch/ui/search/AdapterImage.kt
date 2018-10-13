package com.seongheonson.kakakoimagesearch.ui.search

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.bussines.model.Document
import com.facebook.drawee.view.SimpleDraweeView
import com.seongheonson.kakakoimagesearch.util.DeviceUtil


/**
 * Created by seongheonson on 2018. 10. 12..
 */

class AdapterImage(val documents:MutableList<Document>, context: Context) : RecyclerView.Adapter<ImageViewHolder>() {

    val deviceUtil = DeviceUtil(context)
    var onRepoItemClickListener: ((Document) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        view.layoutParams = RecyclerView.LayoutParams(deviceUtil.width, 500)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = documents.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val document = documents[position]
        holder.imageView.setImageURI(document.image_url)
    }

}

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var imageView = view.findViewById(R.id.image_view) as SimpleDraweeView

}