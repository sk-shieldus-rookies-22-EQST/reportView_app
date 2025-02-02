package com.example.reportview_003.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.api.ViewAPI
import com.example.reportview_003.model.purchase.CartGetItemRequest
import com.example.reportview_003.model.view.ViewbookdetailResponse
import com.example.reportview_003.ui.purchase.action.InsertCartItem
import com.example.reportview_003.ui.view.action.GetBookDetail
import com.example.reportview_003.utils.SessionManager

class BookDetailFragment: Fragment() {

    private lateinit var bookDetailImage : ImageView
    private lateinit var bookDetailTitle : TextView
    private lateinit var bookDetailPrice : TextView
    private lateinit var bookDetailWriter : TextView
    private lateinit var bookDetailContent : TextView
    private lateinit var bookDetailCart : Button
    private lateinit var bookDetailBuy : Button

    private var bookDetailData : MutableMap<String,Any> = mutableMapOf()
    private var bookId : Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.book_detail_main, container, false)

        val app = requireActivity().application as App
        val viewAPI = app.retrofit.create(ViewAPI::class.java)

        val navController = findNavController()

        bookDetailImage = view.findViewById(R.id.book_detail_image)
        bookDetailTitle = view.findViewById(R.id.book_detail_title)
        bookDetailPrice = view.findViewById(R.id.book_detail_price)
        bookDetailWriter = view.findViewById(R.id.book_detail_writer)
        bookDetailCart = view.findViewById(R.id.book_detail_cart)
        bookDetailBuy = view.findViewById(R.id.book_detail_buy)
        bookDetailContent = view.findViewById(R.id.book_detail_content)

        bookId = arguments?.getLong("book_id") ?: -1

        val getBookDetail = GetBookDetail(requireContext(),viewAPI)
        getBookDetail.viewbookdetail(bookId) { response ->
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

        bookDetailCart.setOnClickListener {
            // 장바구니 동작 구현
            if (!SessionManager.isLoggedIn(requireContext())) {
                Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.listFragment) // 루트 페이지로 이동
            } else {
                // 장바구니 동작 구현
                insertCart()
            }
        }

        bookDetailBuy.setOnClickListener {
            // 결제 동작 구현
            if (!SessionManager.isLoggedIn(requireContext())) {
                Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.listFragment) // 루트 페이지로 이동
            } else {
                // 장바구니 동작 구현
                insertCart()
                navController.navigate(R.id.action_bookDetailFragment_to_purchaseFragment)
            }
        }

        return view
    }

    private fun updateUI(viewbookdetailResponse: ViewbookdetailResponse) {
        bookDetailTitle.text = viewbookdetailResponse.title
        bookDetailWriter.text = viewbookdetailResponse.writer
        bookDetailPrice.text = "${viewbookdetailResponse.price} 원" // 가격 예시
        bookDetailContent.text = viewbookdetailResponse.book_summary
        // bookDetailImage는 이미지 URL을 받아 로드하는 로직 필요 (예: Glide, Picasso 사용)
        Glide.with(requireContext())
            .load(viewbookdetailResponse.book_img_path)
            .into(bookDetailImage)
    }

    private fun insertCart() {
        val userId = SessionManager.getUserID(requireContext()).toString()
        val cartGetItemRequest = CartGetItemRequest(
            user_id = userId,
            book_id = bookId
        )
        val app = requireContext().applicationContext as App
        val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)
        val insertCartItem = InsertCartItem(requireContext(), purchaseAPI)
        insertCartItem.insertCartItem(cartGetItemRequest) { response ->
            if (response != null) {
                true
            }
        }
    }
}