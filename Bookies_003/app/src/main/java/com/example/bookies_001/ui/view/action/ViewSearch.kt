package com.example.bookies_001.ui.view.action

import android.content.Context
import com.example.bookies_001.api.ViewAPI
import com.example.bookies_001.model.view.ViewbooksearchRequest
import com.example.bookies_001.model.view.ViewbooksearchResponse
import com.example.bookies_001.repository.ViewBooklist

class ViewSearch(
    private val viewAPI: ViewAPI
) {
    fun search(
        viewbooksearchRequest: ViewbooksearchRequest,
        callback: (ViewbooksearchResponse?, Throwable?) -> Unit ) {
        val viewBooklist = ViewBooklist(viewAPI)

        viewBooklist.viewsearch(viewbooksearchRequest) { response, error ->
            if (response != null) {
                callback(response, null)
            } else {
                error?.printStackTrace()
                callback(null, error)
            }
        }
    }
}