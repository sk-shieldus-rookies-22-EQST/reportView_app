package com.example.reportview_003.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.ViewAPI
import com.example.reportview_003.ui.view.action.*

/*
* 필터링 검색 기능을 가지고 있어야 함
* data에 모든 값을 저장하고 필터링 된 값을 리스트 뷰에 뿌려주는 형식
* */

class ListFragment : Fragment() , View.OnClickListener{

    private lateinit var itemList : ListView
    private lateinit var searchReport : EditText
    private lateinit var searchBtn : ImageButton

    private var renderData : MutableList<MutableMap<String,Any>> = mutableListOf()

    private fun updateUI(data: MutableList<MutableMap<String, Any>>) {
        if (isAdded) { // Fragment가 Activity에 연결되어 있는지 확인
            val context = requireContext() // 안전하게 context 호출
            val adapter = BuildBooklist(context, data)
            itemList.adapter = adapter
        } else {
            Log.e("ListFragment", "Fragment is not attached to a context.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container:ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_main, container, false)

        val app = requireActivity().application as App
        val viewAPI = app.retrofit.create(ViewAPI::class.java)

        itemList = view.findViewById(R.id.list_item)
        searchBtn = view.findViewById(R.id.search_bt)
        searchBtn.setOnClickListener(this)

        val getList = GetList(requireContext(),viewAPI)
        getList.loadBookList { data ->
            if (isAdded) { // Fragment가 Context에 연결된 상태에서만 처리
                renderData = data
                requireActivity().runOnUiThread {
                    updateUI(renderData)
                }
            } else {
                Log.e("ListFragment", "Fragment is not attached to a context while loading data.")
            }
        }

        return view
    }

    override fun onClick(v: View?) {
        searchReport = requireView().findViewById(R.id.search_report)
        renderData = SearchFunction(renderData, searchReport)
        if (isAdded) { // Fragment 상태 확인
            updateUI(renderData)
        } else {
            Log.e("ListFragment", "Fragment is not attached to a context while handling click.")
        }
    }

}