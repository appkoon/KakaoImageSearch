package com.seongheonson.kakakoimagesearch.bussines.model

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
)

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