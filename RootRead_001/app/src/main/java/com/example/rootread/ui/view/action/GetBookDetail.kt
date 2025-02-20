package com.example.rootread.ui.view.action

import android.content.Context
import com.example.rootread.api.ViewAPI
import com.example.rootread.model.view.ViewbookdetailResponse
import com.example.rootread.repository.ViewBooklist

class GetBookDetail(
    private val context: Context,
    private val viewAPI: ViewAPI
) {

    fun viewbookdetail(bookid:Long, callback: (ViewbookdetailResponse?) -> Unit) {
        val bookdetail = ViewBooklist(viewAPI)

        bookdetail.viewbookdetail(bookid) { response, error ->
            if (response != null) {
                val viewbookdetailResponse = ViewbookdetailResponse(
                    book_id = response.book_id,
                    book_summary = response.book_summary?: "No detail available",
                    writer = response.writer?: "No writer available",
                    title = response.title?: "No title available",
                    price = response.price?: "No price available",
                    book_img_path = response.book_img_path?: "",
                    write_date = response.write_date?: "No date available"
                )
                callback(viewbookdetailResponse)
            } else {
                error?.printStackTrace()
            }
        }
    }
}