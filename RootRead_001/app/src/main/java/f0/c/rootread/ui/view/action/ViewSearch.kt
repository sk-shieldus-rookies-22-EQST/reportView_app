package f0.c.rootread.ui.view.action

import f0.c.rootread.api.ViewAPI
import f0.c.rootread.model.view.ViewbooksearchRequest
import f0.c.rootread.model.view.ViewbooksearchResponse
import f0.c.rootread.repository.ViewBooklist

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