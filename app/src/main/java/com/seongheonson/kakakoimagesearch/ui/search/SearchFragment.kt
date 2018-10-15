package com.seongheonson.kakakoimagesearch.ui.search

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.seongheonson.kakakoimagesearch.KEY_DATA
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.business.model.Document
import com.seongheonson.kakakoimagesearch.business.networking.RetryCallback
import com.seongheonson.kakakoimagesearch.business.networking.Status
import com.seongheonson.kakakoimagesearch.databinding.FragmentSearchBinding
import com.seongheonson.kakakoimagesearch.di.Injectable
import com.seongheonson.kakakoimagesearch.ui.Action
import com.seongheonson.kakakoimagesearch.ui.ActionManager
import com.seongheonson.kakakoimagesearch.ui.ActionType
import com.seongheonson.kakakoimagesearch.ui.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by seongheonson on 2018. 10. 12..
 */

class SearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var actionManager: ActionManager

    companion object {
        private const val GRID_COLUMN_COUNT = 1
        fun newInstance(): SearchFragment = SearchFragment()
    }

    private var title = "이미지검색"
    private var refresh = true
    private lateinit var imageAdapter: ImageAdapter
    lateinit var binding: FragmentSearchBinding


    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this)[SearchViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.let{
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayHomeAsUpEnabled(false)
            it.title = title
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.responseLiveData.observe(this, Observer<MutableList<Document>>(::onChanged))
        viewModel.messageLiveData.observe(this, Observer<String>(::onChanged))
        imageAdapter = ImageAdapter(mutableListOf(), activity!!)
        imageAdapter.onRepoItemClickListener = ::onListClicked
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewModel = viewModel
        return binding.root
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
                .subscribe { it ->
                    if (it.isNotEmpty()) {
                       dismissKeyboard()
                       refresh = true
                       viewModel.search(it, true)
                       (activity as MainActivity).supportActionBar?.let{ bar ->
                           title = "이미지검색 (검색어 : $it)"
                           bar.title = title
                           edit_query.setText("")
                       }
                   }
                }

        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }
    }

    private fun initRecyclerView() {
        with(recyclerView) {
//            layoutManager = LinearLayoutManager(context)
            layoutManager = GridLayoutManager(context, GRID_COLUMN_COUNT, GridLayoutManager.VERTICAL, false)
            adapter = imageAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == imageAdapter.itemCount - 1 &&  viewModel.status.get() == Status.SUCCESS) {
                        refresh = false
                        binding.loadingMore = true
                        viewModel.searchNextPage()
                    }
                }
            })
        }
    }

    private fun onChanged(data: Any?) {
        binding.loadingMore = false
        when (data) {
            is String -> {
                Snackbar.make(recyclerView, data, Snackbar.LENGTH_SHORT).show()
            }
            is MutableList<*> -> {
                if (refresh) imageAdapter.documents.clear()
                imageAdapter.documents.addAll(data as MutableList<Document>)
                imageAdapter.notifyDataSetChanged()
                if (refresh) recyclerView.scrollToPosition(0)
            }
        }
    }

    private fun onListClicked(document: Document) {
        val data = Bundle()
        data.putParcelable(KEY_DATA, document)
        actionManager.fire(Action(ActionType.DETAIL_IMAGE, data))
    }

    private fun dismissKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(edit_query.windowToken, 0)
    }

}