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
* */

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
    val formatter = SimpleDateFormat("yyyy-MM-dd")

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

        // 검색버튼 클릭 시
        searchBtn.setOnClickListener {
            // DatePicker로 선택된 날짜 값은 이미 sFilterDate와 eFilterDate에 저장되어 있습니다.
            // 텍스트뷰의 값은 UI용으로만 사용되므로, 여기서는 이를 파싱할 필요가 없습니다.
            if ( (sFilterDate == null) xor (eFilterDate == null) ) {
                Toast.makeText(requireContext(), "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 서버에 보낼 요청 객체 생성 (ViewbooksearchRequest의 sdate, edate 타입은 LocalDateTime? 여야 함)
            val viewSearchRequest = ViewbooksearchRequest(
                keyword = searchReport.text.toString(),
                sdate = sFilterDate,
                edate = eFilterDate
            )

            val viewSearch = ViewSearch(requireContext(), viewAPI)
            viewSearch.search(viewSearchRequest) { response, error ->
                if (response != null) {
                    resData = ViewbooklistResponse(response.book_list)
                    requireActivity().runOnUiThread {
                        updateUI(resData)
                    }
                }
            }
        }

        filterBtn.setOnClickListener {
            // 날짜 선택 리스트 출력: 시작 날짜 ~ 끝 날짜
            showDatePicker()
        }

        return view
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        // 시작 날짜 선택
        DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            { _, year, month, dayOfMonth ->
                val startCalendar = Calendar.getInstance()
                startCalendar.set(year, month, dayOfMonth)
                // UI에 표시할 형식으로 포맷 (yyyy-MM-dd)
                searchSdate.text = formatter.format(startCalendar.time)
                // 서버 전송용 LocalDateTime으로 변환
                sFilterDate = startCalendar.time.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime()

                // 끝 날짜 선택
                DatePickerDialog(
                    requireContext(),
                    R.style.CustomDatePickerDialog,
                    { _, endYear, endMonth, endDayOfMonth ->
                        val endCalendar = Calendar.getInstance()
                        endCalendar.set(endYear, endMonth, endDayOfMonth)
                        searchEdate.text = formatter.format(endCalendar.time)
                        eFilterDate = endCalendar.time.toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime()
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
