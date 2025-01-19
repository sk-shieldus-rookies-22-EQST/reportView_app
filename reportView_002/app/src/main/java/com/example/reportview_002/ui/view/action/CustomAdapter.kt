package com.example.reportview_002.ui.view.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.reportview_002.R

/*
* 리스트 목록을 출력해주는 Adapter
* 현재 로컬에 저장되어 있는지 정보와 제목으로 리스트 뷰를 꾸며서 전달해줌
* */

class CustomAdapter(
    private val context: Context,
    private val data : List<Map<String, Any>>
): BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Map<String, Any> = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val reportName : TextView = view.findViewById(R.id.report_title)
        val fileState : ImageView = view.findViewById(R.id.file_state)

        val item = getItem(position)
        val title = item["title"] as String
        val state = item["state"] as Boolean

        val openAction = FileOpenAction()
        val downloadAction = FileDownloadAction()

        reportName.text = title

        if (state) {
//            파일이 열리는 로직과 연결되어야 함
//            정상적으로 처리된 경우 ReportView로 파일을 전달하여 열 수 있도록 pdf 전달
            fileState.setImageResource(R.drawable.file_open_black)

            reportName.setOnClickListener {
                openAction.openFile(context, title)
            }
            fileState.setOnClickListener {
                openAction.openFile(context, title)
            }
        }
        else {
//            파일을 다운로드 받는 로직과 연결되어야 함
            fileState.setImageResource(R.drawable.download_black)

            reportName.setOnClickListener {
                downloadAction.downloadFile(context, title)
            }
            fileState.setOnClickListener {
                downloadAction.downloadFile(context, title)
            }
        }


        return view
    }

}