package com.example.reportview_002.ui.view.action

import android.widget.EditText


fun SearchFunction(
    data: MutableList<MutableMap<String, Any>>,
    searchReport: EditText
): MutableList<MutableMap<String, Any>> {

    // 검색할 내용
    val keyword = searchReport.text.toString()

    val filteredData = data.filter { item ->
        val title = item["title"] as? String
        title?.contains(keyword, ignoreCase = true) == true
    }

    return filteredData.toMutableList()
}
