package com.seongheonson.kakakoimagesearch.ui.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.seongheonson.kakakoimagesearch.KEY_DATA
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.business.model.Document
import com.seongheonson.kakakoimagesearch.changeDateFormat
import com.seongheonson.kakakoimagesearch.di.Injectable
import com.seongheonson.kakakoimagesearch.ui.ActionManager
import com.seongheonson.kakakoimagesearch.ui.MainActivity
import com.seongheonson.kakakoimagesearch.ui.search.SearchViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.SimpleDateFormat
import javax.inject.Inject

/**
 * Created by seongheonson on 2018. 10. 12..
 */

class DetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val viewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
    }

    companion object {
        fun newInstance(repoBundle: Bundle?): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = repoBundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_detail, null)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.let{
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
            it.title = "이미지 상세보기"
        }

        arguments?.getParcelable<Document>(KEY_DATA)?.let { document ->
            draweeView.setImageURI(document.image_url)
            text_size.text = "이미지 크기 : ${document.width} x ${document.height}"
            text_sitename.text = "이미지 출처 : ${document.display_sitename}\n(${document.doc_url})"
            text_date.text = "작성일시 : ${changeDateFormat(document.datetime)}"
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
                    android.R.id.home -> {
                        activity?.onBackPressed()
                        true
                    }
                    else -> super.onOptionsItemSelected(item);
              }
    }
}