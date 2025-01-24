package com.example.reportview_003.ui.view.action

import android.content.Context
import com.example.reportview_003.api.ViewAPI
import com.example.reportview_003.model.view.ViewbookdetailResponse
import com.example.reportview_003.repository.ViewBooklist

class GetBookDetail(
    private val context: Context,
    private val viewAPI: ViewAPI
) {

    fun viewbookdetail(bookid:Int, callback: (ViewbookdetailResponse?) -> Unit) {
        val bookdetail = ViewBooklist(viewAPI)

        bookdetail.viewbookdetail(bookid) { response, error ->
            if (response != null) {
                val viewbookdetailResponse = ViewbookdetailResponse(
                    book_id = response.book_id?: 0,
                    detail = response.detail?: "No detail available",
                    writer = response.writer?: "No writer available",
                    title = response.title?: "No title available",
                    price = response.price?: "No price available"
                )
                callback(viewbookdetailResponse)
            } else {
                error?.printStackTrace()
            }
        }
    }
}