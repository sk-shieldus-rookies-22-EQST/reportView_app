package f0.c.rootread.repository

import f0.c.rootread.api.ViewAPI
import f0.c.rootread.model.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewBooklist(private val api: ViewAPI) {

    fun viewbooklist(
        callback: (ViewbooklistResponse?, Throwable?) -> Unit
    ) {
        api.viewbooklist().enqueue(object : Callback<ViewbooklistResponse> {
            override fun onResponse(
                call: Call<ViewbooklistResponse>,
                response: Response<ViewbooklistResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Request failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ViewbooklistResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun viewsearch(viewbookviewerRequest:ViewbooksearchRequest, callback: (ViewbooksearchResponse?, Throwable?) -> Unit) {
        api.viewsearch(viewbookviewerRequest).enqueue(object : Callback<ViewbooksearchResponse> {
            override fun onResponse(call: Call<ViewbooksearchResponse>, response: Response<ViewbooksearchResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ViewbooksearchResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun viewbookdetail(bookid:Long, callback: (ViewbookdetailResponse?, Throwable?) -> Unit) {
        api.viewbookdetail(bookid.toString()).enqueue(object : Callback<ViewbookdetailResponse> {
            override fun onResponse(call: Call<ViewbookdetailResponse>, response: Response<ViewbookdetailResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ViewbookdetailResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun viewbookviewer(viewbookviewerRequest:ViewbookviewerRequest, callback: (ViewbookviewerResponse?, Throwable?) -> Unit) {
        api.viewbookviewer(viewbookviewerRequest).enqueue(object : Callback<ViewbookviewerResponse> {
            override fun onResponse(call: Call<ViewbookviewerResponse>, response: Response<ViewbookviewerResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ViewbookviewerResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}