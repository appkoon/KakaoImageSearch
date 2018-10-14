package com.seongheonson.kakakoimagesearch.ui.search

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.business.model.Document


/**
 * Created by seongheonson on 2018. 10. 12..
 */

class ImageAdapter(val documents:MutableList<Document>, context: Context) : RecyclerView.Adapter<ImageHolder>() {

    private val displayWidth = context.resources.displayMetrics!!.widthPixels
    var onRepoItemClickListener: ((Document) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val context = parent.context
        val hierarchy = GenericDraweeHierarchyBuilder(context.resources)
            .setPlaceholderImage(ColorDrawable(ContextCompat.getColor(context, R.color.load_placeholder)))
            .setFailureImage(ColorDrawable(ContextCompat.getColor(context, R.color.load_fail)))
            .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
            .build()
        return ImageHolder(SimpleDraweeView(context, hierarchy), displayWidth)
    }

    override fun getItemCount(): Int = documents.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val document = documents[position]
        val layoutParam = holder.itemView.layoutParams
        val imageWidth = document.width.toFloat()
        val imageHeight = document.height.toFloat()
        val rate = (displayWidth.toFloat() / imageWidth)
        val resizeHeight = (imageHeight * rate)
        Log.e("good", "getResize = $imageWidth x $imageHeight = ${imageWidth / imageHeight} " +
                                 "/ $displayWidth x $resizeHeight = ${displayWidth.toFloat() / resizeHeight}")
        layoutParam.height = resizeHeight.toInt()
        holder.itemView.requestLayout()
        holder.bind(Uri.parse(document.image_url))
        holder.itemView.setOnClickListener { _ ->
            onRepoItemClickListener?.let { it(documents[holder.adapterPosition]) }
        }
    }

}

data class ImageHolder(private val view: View, private val displayWidth: Int) : RecyclerView.ViewHolder(view) {

    init {
        val param = ViewGroup.MarginLayoutParams(displayWidth, 0)
        param.setMargins(0, 10 ,0 ,10)
        itemView.layoutParams = param
    }

    fun bind(uri: Uri) {
        itemView as? SimpleDraweeView ?: return
        itemView.controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri).build())
                .setOldController(itemView.controller)
                .setAutoPlayAnimations(true)
                .build()
    }

}