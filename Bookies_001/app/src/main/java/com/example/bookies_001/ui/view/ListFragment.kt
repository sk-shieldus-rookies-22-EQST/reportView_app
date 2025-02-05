package com.example.bookies_001.ui.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.ViewAPI
import com.example.bookies_001.model.view.ViewbooklistResponse
import com.example.bookies_001.model.view.ViewbooksearchRequest
import com.example.bookies_001.ui.view.action.BuildBooklist
import com.example.bookies_001.ui.view.action.GetList
import com.example.bookies_001.ui.view.action.ViewSearch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar

/*
* 필터링 검색 기능을 가지고 있어야 함
* data에 모든 값을 저장하고 필터링 된 값을 리스트 뷰에 뿌려주는 형식
*/

class ListFragment : Fragment() {

    private lateinit var itemList: ListView
    private lateinit var searchReport: EditText
    private lateinit var searchBtn: ImageView
    private lateinit var filterBtn: ImageView
    private lateinit var searchSdate: TextView
    private lateinit var searchEdate: TextView

    private lateinit var resData: ViewbooklistResponse

    // 서버 전송용 날짜를 저장할 변수 (LocalDateTime 타입)
    private var sFilterDate: LocalDateTime? = null
    private var eFilterDate: LocalDateTime? = null

    // UI에 표시할 형식 ("yyyy-MM-dd")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    private fun updateUI(data: ViewbooklistResponse) {
        if (isAdded) { // Fragment가 Activity에 연결되어 있는지 확인
            val context = requireContext() // 안전하게 context 호출
            val navController = findNavController()
            val adapter = BuildBooklist(context, data.book_list, navController)
            itemList.adapter = adapter
        } else {
            Log.e("ListFragment", "Fragment is not attached to a context.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_main, container, false)

        val app = requireActivity().application as App
        val viewAPI = app.retrofit.create(ViewAPI::class.java)

        itemList = view.findViewById(R.id.list_item)
        searchBtn = view.findViewById(R.id.search_bt)
        filterBtn = view.findViewById(R.id.filter_bt)
        searchSdate = view.findViewById(R.id.search_sdate)
        searchEdate = view.findViewById(R.id.search_edate)
        searchReport = view.findViewById(R.id.search_report)

        val getList = GetList(requireContext(), viewAPI)
        getList.loadBookList { data ->
            if (isAdded) { // Fragment가 Context에 연결된 상태에서만 처리
                resData = data
                requireActivity().runOnUiThread {
                    updateUI(resData)
                }
            } else {
                Log.e("ListFragment", "Fragment is not attached to a context while loading data.")
            }
        }

        // 검색 버튼 클릭 시
        searchBtn.setOnClickListener {
            Log.d("ListFragment", "검색 버튼 클릭 - sFilterDate: $sFilterDate, eFilterDate: $eFilterDate")

            if ((sFilterDate == null) xor (eFilterDate == null)) {
                Toast.makeText(requireContext(), "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val viewSearchRequest = ViewbooksearchRequest.fromLocalDateTime(
                keyword = searchReport.text.toString(),
                sdate = sFilterDate,
                edate = eFilterDate
            )

            Log.d("ListFragment", "검색 요청 JSON: $viewSearchRequest")

            val viewSearch = ViewSearch(requireContext(), viewAPI)
            viewSearch.search(viewSearchRequest) { response, error ->
                if (response != null) {
                    resData = ViewbooklistResponse(response.book_list)
                    requireActivity().runOnUiThread {
                        updateUI(resData)
                    }
                } else {
                    Log.e("ListFragment", "검색 요청 실패: ${error?.message}")
                }
            }
        }

        filterBtn.setOnClickListener {
            // 날짜 선택 리스트 출력: 시작 날짜 ~ 끝 날짜
            showStartDatePicker()
        }

        return view
    }

    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            { _, year, month, dayOfMonth ->
                val startCalendar = Calendar.getInstance()
                startCalendar.set(year, month, dayOfMonth)

                // UI에 표시할 형식으로 포맷 (yyyy-MM-dd)
                searchSdate.text = formatter.format(startCalendar.time)

                // LocalDateTime 변환 (변환 과정 개선)
                sFilterDate = LocalDateTime.of(
                    year, month + 1, dayOfMonth, 0, 0
                )

                Log.d("ListFragment", "시작 날짜 선택됨: $sFilterDate")

                // 끝 날짜 선택
                showEndDatePicker()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showEndDatePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            { _, endYear, endMonth, endDayOfMonth ->
                val endCalendar = Calendar.getInstance()
                endCalendar.set(endYear, endMonth, endDayOfMonth)

                // UI에 표시할 형식으로 포맷
                searchEdate.text = formatter.format(endCalendar.time)

                // LocalDateTime 변환 (변환 과정 개선)
                eFilterDate = LocalDateTime.of(
                    endYear, endMonth + 1, endDayOfMonth, 23, 59
                )

                Log.d("ListFragment", "끝 날짜 선택됨: $eFilterDate")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
