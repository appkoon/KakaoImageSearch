package com.seongheonson.kakakoimagesearch.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.util.Log
import com.seongheonson.kakakoimagesearch.vo.Document
import com.seongheonson.kakakoimagesearch.vo.ImageSearch
import com.seongheonson.kakakoimagesearch.api.ApiResponse
import com.seongheonson.kakakoimagesearch.api.Error
import com.seongheonson.kakakoimagesearch.api.ApiRequest
import com.seongheonson.kakakoimagesearch.api.Status
import com.seongheonson.kakakoimagesearch.repository.KakaoRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by seongheonson on 2018. 10. 13..
 */
@Singleton
class SearchViewModel @Inject constructor(private val repository: KakaoRepository) : ViewModel() {

    private var page = 1
    private var size = 10
    var query = ""
    private var isEnd = false

    val messageLiveData: LiveData<String> by lazy { MutableLiveData<String>() }
    val responseLiveData: LiveData<List<Document>> by lazy { MutableLiveData<List<Document>>() }

    val status = ObservableField<Status>()
    val dataCount = ObservableInt()
    val errorText = ObservableField<String>()


    @SuppressLint("CheckResult")
    fun search(query: String, reset: Boolean) {
        setStatus(Status.LOADING)
        if (reset) {
            page = 1
            isEnd = false
            this.query = query
            dataCount.set(0)
        }
        ApiRequest.request(repository.search(query, page, size), object : ApiResponse<ImageSearch>{
            override fun onSuccess(response: ImageSearch) {
                Log.e("good", "page = $page isEnd = ${response.meta.is_end} documents = ${response.documents.size}")
                if (!isEnd){
                    Handler().postDelayed({
                        setStatus(Status.SUCCESS)
                        dataCount.set(response.documents.size)
                        if (dataCount.get() > 0) {
                            (responseLiveData as MutableLiveData<*>).value = response.documents
                            isEnd = response.meta.is_end
                            page++
                        }
                    }, 1000)
                } else {
                    (messageLiveData as MutableLiveData<*>).value = Error.NO_MORE_DATA.value
                }
            }
            override fun onError(throwable: Throwable) {
                when (throwable) {
                    is HttpException -> setStatus(Status.ERROR, Error.UNKNOWN.value)
                    is SocketTimeoutException -> setStatus(Status.ERROR, Error.TIMEOUT.value)
                    is IOException -> setStatus(Status.ERROR, Error.DISCONNECTED.value)
                    else -> setStatus(Status.ERROR, Error.UNKNOWN.value)
                }
            }
            override fun onServerError(errorMessage: String) {
                setStatus(Status.ERROR, errorMessage)
            }
        })
    }


    fun searchNextPage() {
        search(query, false)
    }


    fun retry() {
        search(query, true)
    }


    private fun setStatus(status: Status, message: String? = null){
        this.status.set(status)
        if (message != null) this.errorText.set(message)
    }


}