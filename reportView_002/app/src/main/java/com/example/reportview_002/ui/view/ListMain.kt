package com.example.reportview_002.ui.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.reportview_002.R
import com.example.reportview_002.ui.view.action.*

/*
* 필터링 검색 기능을 가지고 있어야 함
* data에 모든 값을 저장하고 필터링 된 값을 리스트 뷰에 뿌려주는 형식
* */

class ListMain : AppCompatActivity() , View.OnClickListener{

    lateinit var itemList : ListView
    lateinit var searchReport : EditText
    lateinit var searchBtn : ImageButton

    val data : MutableList<MutableMap<String,Any>> = mutableListOf(
        mutableMapOf("title" to "report 1", "state" to true),
        mutableMapOf("title" to "report 2", "state" to false),
        mutableMapOf("title" to "report 3", "state" to false),
        mutableMapOf("title" to "report 4", "state" to false),
        mutableMapOf("title" to "report 5", "state" to true),
        mutableMapOf("title" to "report 6", "state" to true),
        mutableMapOf("title" to "report 7", "state" to false),
        mutableMapOf("title" to "report 8", "state" to true),
        mutableMapOf("title" to "report 9", "state" to true),
        mutableMapOf("title" to "report 10", "state" to true),
        mutableMapOf("title" to "report 11", "state" to false),
        mutableMapOf("title" to "report 12", "state" to false),
        mutableMapOf("title" to "report 13", "state" to true),
        mutableMapOf("title" to "report 14", "state" to true),
        mutableMapOf("title" to "report 15", "state" to false),
        mutableMapOf("title" to "report 16", "state" to false),
        mutableMapOf("title" to "report 17", "state" to false),
        mutableMapOf("title" to "report 1", "state" to true),
        mutableMapOf("title" to "report 2", "state" to false),
        mutableMapOf("title" to "report 3", "state" to false),
        mutableMapOf("title" to "report 4", "state" to false),
        mutableMapOf("title" to "report 5", "state" to true),
        mutableMapOf("title" to "report 6", "state" to true),
        mutableMapOf("title" to "report 7", "state" to false),
        mutableMapOf("title" to "report 8", "state" to true),
        mutableMapOf("title" to "report 9", "state" to true),
        mutableMapOf("title" to "report 10", "state" to true),
        mutableMapOf("title" to "report 11", "state" to false),
        mutableMapOf("title" to "report 12", "state" to false),
        mutableMapOf("title" to "report 13", "state" to true),
        mutableMapOf("title" to "report 14", "state" to true),
        mutableMapOf("title" to "report 15", "state" to false),
        mutableMapOf("title" to "report 16", "state" to false),
        mutableMapOf("title" to "report 17", "state" to false)
    )
    lateinit var renderData : MutableList<MutableMap<String,Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_main)

        itemList = findViewById(R.id.list_item)
        itemList.adapter = CustomAdapter(this, data)


        searchBtn = findViewById(R.id.search_bt)

        searchBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        searchReport = findViewById(R.id.search_report)
        renderData = SearchFunction(data, searchReport)
        itemList.adapter = CustomAdapter(this, renderData)
    }

}