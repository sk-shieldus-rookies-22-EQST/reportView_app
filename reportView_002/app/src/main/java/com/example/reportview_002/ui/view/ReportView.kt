package com.example.reportview_002.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reportview_002.R

/*
* PDF 랜더링 할 수 있는 페이지
* 캡쳐 방지
* */

class ReportView : AppCompatActivity() {

    fun OnCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_view)

    }
}