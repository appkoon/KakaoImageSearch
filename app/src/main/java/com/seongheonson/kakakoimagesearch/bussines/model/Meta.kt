package com.seongheonson.kakakoimagesearch.bussines.model

/**
 * Created by seongheonson on 2018. 10. 12..
 */

data class Meta(
        private var total_count: Int,
        private var pageable_count: Int,
        private var is_end: Boolean
)

    /**
     * - Meta
        total_count	    검색어에 검색된 문서수	                Integer
        pageable_count	total_count 중에 노출가능 문서수	        Integer
        is_end	현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음.	Boolean
    */
