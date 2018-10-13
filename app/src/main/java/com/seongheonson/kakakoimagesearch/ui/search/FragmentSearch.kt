package com.seongheonson.kakakoimagesearch.ui.search

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.bussines.model.Document
import com.seongheonson.kakakoimagesearch.bussines.networking.Error
import com.seongheonson.kakakoimagesearch.bussines.repository.KakaoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.loading_state.*
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by seongheonson on 2018. 10. 12..
 */

class FragmentSearch : Fragment() {

    private var isLoading = false
    private var refresh = true
    private lateinit var adapter: AdapterImage


    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AdapterImage(mutableListOf(), activity!!)
        adapter.onRepoItemClickListener = ::onRepoClicked
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_search, null)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        RxTextView.afterTextChangeEvents(edit_query)
                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map { it.editable().toString() }
                .subscribe {
                   if (it.isNotEmpty()) {
                       dismissKeyboard()
                       refresh = true
                       text_error.visibility = View.GONE
                       recyclerView.visibility = View.GONE
                       progress_search.visibility = View.VISIBLE
                       searchViewModel.search(it, true)
                   }
                }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1 && !isLoading) {
                    refresh = false
                    isLoading = true
                    searchViewModel.searchNextPage()
                    progress_load.visibility = View.VISIBLE
                }
            }
        })
        searchViewModel.responseLiveData.observe(this, Observer<MutableList<Document>>(::onChanged))
        searchViewModel.errorLiveData.observe(this, Observer<Error>(::onChanged))
    }

    private fun onChanged(data: Any?) {
        Handler().postDelayed({
            progress_load.visibility = View.GONE
            progress_search.visibility = View.GONE
            when (data) {
                is Error -> {
                    when (data) {
                        Error.NO_MORE_DATA -> {
                            Snackbar.make(recyclerView, Error.NO_MORE_DATA.value, Snackbar.LENGTH_SHORT).show()
                        }
                        Error.NO_DATA -> {
                            text_error.text = Error.NO_DATA.value
                            text_error.visibility = View.VISIBLE
                        }
                        Error.UNKNOWN, Error.TIMEOUT -> {
                            text_error.text = "서버 오류"
                            text_error.visibility = View.VISIBLE
                        }
                        Error.DISCONNECTED -> {
                            text_error.text = Error.DISCONNECTED.value
                            text_error.visibility = View.VISIBLE
                        }
                    }
                }
                is MutableList<*> -> {
                    recyclerView.visibility = View.VISIBLE
                    if (refresh) adapter.documents.clear()
                    adapter.documents.addAll(data as MutableList<Document>)
                    adapter.notifyDataSetChanged()
                    if (refresh) recyclerView.scrollToPosition(0)
                    isLoading = false
                }
            }
        }, 1000)

    }

    private fun onRepoClicked(document: Document) {
//        val data = Bundle()
//        data.putParcelable(KEY_DATA, repo)
//        actionManager.fire(Action(ActionType.ACTION_REPO, data))
    }

    private fun dismissKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(edit_query.windowToken, 0)
    }
}