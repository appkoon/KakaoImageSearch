package com.seongheonson.kakakoimagesearch.ui.search

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.vo.Document
import com.seongheonson.kakakoimagesearch.common.getResizedHeight


/**
 * Created by seongheonson on 2018. 10. 12..
 */

class ImageAdapter(private val documents:MutableList<Document>, context: Context) : RecyclerView.Adapter<ImageHolder>() {

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
        layoutParam.height = getResizedHeight(document.width, document.height, displayWidth)
        holder.itemView.requestLayout()
        holder.bind(Uri.parse(document.image_url))
        holder.itemView.setOnClickListener { _ ->
            onRepoItemClickListener?.let { it(documents[holder.adapterPosition]) }
        }
    }

    fun setData(documents: List<Document>, refreshList: Boolean) {
        if (refreshList) this.documents.clear()
        this.documents.addAll(documents)
        notifyDataSetChanged()
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