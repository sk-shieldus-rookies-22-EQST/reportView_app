package f0.c.rootread.ui.view.action

import android.content.Context
import f0.c.rootread.api.ViewAPI
import f0.c.rootread.model.view.ViewbooklistResponse
import f0.c.rootread.repository.ViewBooklist

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
