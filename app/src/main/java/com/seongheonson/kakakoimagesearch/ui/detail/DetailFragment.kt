package com.seongheonson.kakakoimagesearch.ui.detail

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.seongheonson.kakakoimagesearch.common.KEY_DATA
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.databinding.FragmentDetailBinding
import com.seongheonson.kakakoimagesearch.di.Injectable
import com.seongheonson.kakakoimagesearch.ui.MainActivity
import com.seongheonson.kakakoimagesearch.vo.Document

/**
 * Created by seongheonson on 2018. 10. 12..
 */

class DetailFragment : Fragment(), Injectable {

    lateinit var binding: FragmentDetailBinding

    companion object {
        fun newInstance(repoBundle: Bundle?): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = repoBundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
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
//            draweeView.setImageURI(document.image_url)
//            text_size.text = "이미지 크기 : ${document.width} x ${document.height}"
//            text_sitename.text = "이미지 출처 : ${document.display_sitename}\n(${document.doc_url})"
//            text_date.text = "작성일시 : ${changeDateFormat(document.datetime)}"
            binding.document = document
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