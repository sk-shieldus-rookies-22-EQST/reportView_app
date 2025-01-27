package com.example.reportview_003.ui.view.action

import android.content.Context
import com.example.reportview_003.api.ViewAPI
import com.example.reportview_003.model.view.ViewbooksearchRequest
import com.example.reportview_003.model.view.ViewbooksearchResponse
import com.example.reportview_003.repository.ViewBooklist
import retrofit2.Callback

class ViewSearch(
    private val context: Context,
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