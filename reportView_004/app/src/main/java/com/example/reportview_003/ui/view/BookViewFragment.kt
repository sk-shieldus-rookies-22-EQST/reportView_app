package com.example.reportview_003.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reportview_003.R
//import com.github.barteksc.pdfviewer.PDFView

/*
* PDF 랜더링 할 수 있는 페이지
* 캡쳐 방지
* */

class BookViewFragment : Fragment() {

    companion object {
        private const val ARG_USER_INPUT = "userInput"
        private const val ARG_REPORT_NAME = "reportName"

        // 새로운 인스턴스를 생성하며 필요한 데이터를 전달
        fun newInstance(userInput: String, reportName: String): BookViewFragment {
            val fragment = BookViewFragment()
            val args = Bundle().apply {
                putString(ARG_USER_INPUT, userInput)
                putString(ARG_REPORT_NAME, reportName)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var userInput: String
    private lateinit var reportName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userInput = it.getString(ARG_USER_INPUT) ?: ""
            reportName = it.getString(ARG_REPORT_NAME) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.book_view, container, false)
//        val pdfView: PDFView = view.findViewById(R.id.pdfView)

        // PDF 파일 로드 (assets 폴더)
//        pdfView.fromAsset("sample.pdf")
//            .defaultPage(0) // 시작 페이지
//            .enableSwipe(true) // 스와이프 가능
//            .enableAnnotationRendering(true) // 주석 렌더링
//            .load()

        return view
    }

}