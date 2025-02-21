package com.example.rootread.ui.view.action

import android.widget.EditText
import com.example.rootread.model.view.ViewbooklistResponse


fun SearchFunction(
    data: ViewbooklistResponse,
    searchReport: EditText
): ViewbooklistResponse {

    // 검색할 내용
    val keyword = searchReport.text.toString()

    val filteredData = data.book_list.filter { item ->
        val title = item.title as? String
        title?.contains(keyword, ignoreCase = true) == true
    }

    return ViewbooklistResponse(
        book_list = filteredData.toMutableList()
    )
}
