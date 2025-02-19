package com.example.bookies_001.ui.view

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.api.ViewAPI
import com.example.bookies_001.model.purchase.CartGetItemRequest
import com.example.bookies_001.model.view.ViewbookdetailResponse
import com.example.bookies_001.ui.purchase.action.InsertCartItem
import com.example.bookies_001.ui.view.action.GetBookDetail
import com.example.bookies_001.utils.SessionManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.coroutines.resume

class BookDetailFragment: Fragment() {

    private lateinit var bookDetailImage : ImageView
    private lateinit var bookDetailTitle : TextView
    private lateinit var bookDetailPrice : TextView
    private lateinit var bookDetailWriter : TextView
    private lateinit var bookDetailContent : TextView
    private lateinit var bookDetailDate : TextView
    private lateinit var bookDetailCart : Button
    private lateinit var bookDetailBuy : Button

    private var bookDetailData : MutableMap<String,Any> = mutableMapOf()
    private var bookId : Long = -1L
    // LocalDateTime 입력 포맷: "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS+SS:SS")
    // 출력 포맷: "yyyy-MM-dd"
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
        bookDetailDate = view.findViewById(R.id.book_detail_date)
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
                lifecycleScope.launch {
                    val isInserted = insertCart()
                    // 장바구니 동작 구현
                    if(isInserted) {
                        Toast.makeText(requireContext(),"장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(),"장바구니에 담기 실패 했습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        bookDetailBuy.setOnClickListener {
            // 결제 동작 구현
            if (!SessionManager.isLoggedIn(requireContext())) {
                Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.listFragment) // 루트 페이지로 이동
            } else {
                lifecycleScope.launch {
                    val isInserted = insertCart()
                    // 장바구니 동작 구현
                    if (isInserted) {
                        navController.navigate(R.id.action_bookDetailFragment_to_purchaseFragment)
                    } else {
                        Toast.makeText(requireContext(), "구매 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

    private fun updateUI(viewbookdetailResponse: ViewbookdetailResponse) {
        bookDetailTitle.text = viewbookdetailResponse.title
        bookDetailWriter.text = viewbookdetailResponse.writer
        bookDetailDate.text = try {
            val parsedDate = LocalDateTime.parse(viewbookdetailResponse.write_date, inputFormatter)
            parsedDate.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            Log.e("EachBoardFragment", "Date parsing error: ${e.message}")
            "날짜 없음"
        }
        bookDetailPrice.text = "${NumberFormat.getNumberInstance(Locale.US).format(viewbookdetailResponse.price.toInt())} 원" // 가격 예시 "${NumberFormat.getNumberInstance(Locale.US).format(price)} 원"
        bookDetailContent.text = viewbookdetailResponse.book_summary
        // bookDetailImage는 이미지 URL을 받아 로드하는 로직 필요 (예: Glide, Picasso 사용)
       val IMG_PATH = "https://3.35.84.46" + viewbookdetailResponse.book_img_path
        Glide.with(requireContext())
            .load(IMG_PATH)
            .placeholder(R.drawable.bookkies_icon_thick)
            .into(bookDetailImage)
    }

    private suspend fun insertCart(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val userId = SessionManager.getUserID(requireContext()).toString()
            val cartGetItemRequest = CartGetItemRequest(
                user_id = userId,
                book_id = bookId
            )

            val app = requireContext().applicationContext as App
            val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)
            val insertCartItem = InsertCartItem(requireContext(), purchaseAPI)

            insertCartItem.insertCartItem(cartGetItemRequest) { response ->
                continuation.resume(response?.status ?: false)
            }
        }
    }
}