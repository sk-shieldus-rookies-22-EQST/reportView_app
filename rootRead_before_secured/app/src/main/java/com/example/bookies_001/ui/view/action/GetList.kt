package com.example.bookies_001.ui.view.action

import android.content.Context
import com.example.bookies_001.api.ViewAPI
import com.example.bookies_001.model.view.ViewbooklistResponse
import com.example.bookies_001.repository.ViewBooklist

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
