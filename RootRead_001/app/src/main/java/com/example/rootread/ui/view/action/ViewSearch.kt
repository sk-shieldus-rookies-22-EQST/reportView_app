package com.example.rootread.ui.view.action

import com.example.rootread.api.ViewAPI
import com.example.rootread.model.view.ViewbooksearchRequest
import com.example.rootread.model.view.ViewbooksearchResponse
import com.example.rootread.repository.ViewBooklist

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