package com.example.reportview_003.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.ViewAPI
import com.example.reportview_003.model.view.ViewbookdetailResponse
import com.example.reportview_003.ui.view.action.GetBookDetail

class BookDetailFragment: Fragment() {

    private lateinit var bookDetailImage : ImageView
    private lateinit var bookDetailTitle : TextView
    private lateinit var bookDetailPrice : TextView
    private lateinit var bookDetailWriter : TextView
    private lateinit var bookDetailContent : TextView
    private lateinit var bookDetailCart : Button

    private var bookDetailData : MutableMap<String,Any> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.book_detail_main, container, false)

        val app = requireActivity().application as App
        val viewAPI = app.retrofit.create(ViewAPI::class.java)

        bookDetailImage = view.findViewById(R.id.book_detail_image)
        bookDetailTitle = view.findViewById(R.id.book_detail_title)
        bookDetailPrice = view.findViewById(R.id.book_detail_price)
        bookDetailWriter = view.findViewById(R.id.book_detail_writer)
        bookDetailCart = view.findViewById(R.id.book_detail_cart)
        bookDetailContent = view.findViewById(R.id.book_detail_content)

        val getBookDetail = GetBookDetail(requireContext(),viewAPI)
        getBookDetail.viewbookdetail(1) { response ->
            if (isAdded) {
                if (response != null){
                    updateUI(response)
                } else {
                    Log.e("BookDetailFragment", "Fragment is not attached to a context while loading data.")
                }
            } else {
                Log.e("BookDetailFragment", "Fragment is not attached to a context while loading data.")
            }
        }

        return view
    }

    private fun updateUI(viewbookdetailResponse: ViewbookdetailResponse) {
        bookDetailTitle.text = viewbookdetailResponse.title
        bookDetailWriter.text = viewbookdetailResponse.writer
        bookDetailPrice.text = "${viewbookdetailResponse.price} 원" // 가격 예시
        bookDetailContent.text = viewbookdetailResponse.detail
        // bookDetailImage는 이미지 URL을 받아 로드하는 로직 필요 (예: Glide, Picasso 사용)
    }
}