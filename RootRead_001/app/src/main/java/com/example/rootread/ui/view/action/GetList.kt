package com.example.rootread.ui.view.action

import android.content.Context
import com.example.rootread.api.ViewAPI
import com.example.rootread.model.view.ViewbooklistResponse
import com.example.rootread.repository.ViewBooklist

class GetList(
    private val context: Context,
    private val viewAPI: ViewAPI
) {
    fun loadBookList(callback: (ViewbooklistResponse) -> Unit) {
        val booklist = ViewBooklist(viewAPI)

        booklist.viewbooklist { response , error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}
