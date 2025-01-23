package com.example.reportview_003.ui.view.action

import android.content.Context
import com.example.reportview_003.api.ViewAPI
import com.example.reportview_003.repository.ViewBooklist

class GetList(
    private val context: Context,
    private val viewAPI: ViewAPI
) {
    fun loadBookList(callback: (MutableList<MutableMap<String, Any>>) -> Unit) {
        val booklist = ViewBooklist(viewAPI)

        booklist.viewbooklist { response, error ->
            if (response != null) {
                callback(response.book_list)
            } else {
                error?.printStackTrace()
            }
        }
    }
}
