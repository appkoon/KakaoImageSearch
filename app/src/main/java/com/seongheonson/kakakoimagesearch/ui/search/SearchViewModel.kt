package com.seongheonson.kakakoimagesearch.ui.search

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.util.Log
import com.seongheonson.kakakoimagesearch.business.model.Document
import com.seongheonson.kakakoimagesearch.business.model.ImageSearch
import com.seongheonson.kakakoimagesearch.business.networking.ApiListener
import com.seongheonson.kakakoimagesearch.business.networking.Error
import com.seongheonson.kakakoimagesearch.business.networking.RetrofitHelper
import com.seongheonson.kakakoimagesearch.business.networking.Status
import com.seongheonson.kakakoimagesearch.business.repository.KakaoRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


/**
 * Created by seongheonson on 2018. 10. 13..
 */

class SearchViewModel(val app: Application) : AndroidViewModel(app) {

    private val kakaoRepository = KakaoRepository()

    private var page = 1
    private var size = 20
    var query = ""
    private var isEnd = false

    val messageLiveData: LiveData<String> by lazy { MutableLiveData<String>() }
    val responseLiveData: LiveData<MutableList<Document>> by lazy { MutableLiveData<MutableList<Document>>() }

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
        RetrofitHelper.request(kakaoRepository.search(query, page, size), object : ApiListener<ImageSearch>{
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
                    (messageLiveData as MutableLiveData<*>).value = Error.NO_MORE_DATA
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