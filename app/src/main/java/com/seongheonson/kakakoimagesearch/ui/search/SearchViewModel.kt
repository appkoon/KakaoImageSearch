package com.seongheonson.kakakoimagesearch.ui.search

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.Observable
import android.databinding.ObservableField
import com.seongheonson.kakakoimagesearch.bussines.model.Document
import com.seongheonson.kakakoimagesearch.bussines.model.Resource
import com.seongheonson.kakakoimagesearch.bussines.model.Response
import com.seongheonson.kakakoimagesearch.bussines.networking.Error
import com.seongheonson.kakakoimagesearch.bussines.repository.KakaoRepository
import com.seongheonson.kakakoimagesearch.util.AbsentLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.util.Log


/**
 * Created by seongheonson on 2018. 10. 13..
 */

class SearchViewModel(val app: Application) : AndroidViewModel(app) {

    val repository = KakaoRepository()

    private var page = 1
    private var size = 20
    private var pageMax = 0
    private var query = ""
    private var isEnd = false

    init {
//        Transformations.map(query) { if (!it.isNullOrBlank()) search(it, true) }
    }

    val errorLiveData: LiveData<Error> by lazy { MutableLiveData<Error>() }
    val responseLiveData: LiveData<MutableList<Document>> by lazy { MutableLiveData<MutableList<Document>>() }


    @SuppressLint("CheckResult")
    fun search(query: String, reset: Boolean) {
        if (reset) {
            page = 1
            isEnd = false
            this.query = query
        }
        repository.search(query, page, size)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe({
                    Log.e("good", "page = $page/${it.meta.pageable_count} isEnd = ${it.meta.is_end} documents = ${it.documents.size}")
                    if (!isEnd){
                      if (it.documents.isNotEmpty()) {
                          (responseLiveData as MutableLiveData<*>).value = it.documents
                          isEnd = it.meta.is_end
                          page++
                      } else {
                          (errorLiveData as MutableLiveData<*>).value = Error.NO_DATA
                      }
                    } else {
                        (errorLiveData as MutableLiveData<*>).value = Error.NO_MORE_DATA
                    }
                }, { error ->
                    (errorLiveData as MutableLiveData<*>).value = when (error) {
                        is HttpException -> Error.UNKNOWN
                        is SocketTimeoutException -> Error.TIMEOUT
                        is IOException -> Error.DISCONNECTED
                        else -> Error.UNKNOWN
                    }
                })
    }

    fun searchNextPage() {
        search(query, false)
    }
}