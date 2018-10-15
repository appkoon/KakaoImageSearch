package com.seongheonson.kakakoimagesearch.vo

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by seongheonson on 2018. 10. 12..
 */

data class Document (
        var collection: String,
        var thumbnail_url: String,
        var image_url: String,
        var width: Int,
        var height: Int,
        var display_sitename: String,
        var doc_url: String,
        var datetime: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(collection)
        parcel.writeString(thumbnail_url)
        parcel.writeString(image_url)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeString(display_sitename)
        parcel.writeString(doc_url)
        parcel.writeString(datetime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Document> {
        override fun createFromParcel(parcel: Parcel): Document {
            return Document(parcel)
        }

        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)
        }
    }

}

/*


    - document
    collection	        컬렉션	            String
    thumbnail_url	    이미지 썸네일 URL	    String
    image_url	        이미지 URL	        String
    width	            이미지의 가로 크기	    Integer
    height	            이미지의 세로 크기	    Integer
    display_sitename	출처명	            String
    doc_url	            문서 URL	            String
    datetime	        문서 작성시간. ISO 8601. [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]	String
*/