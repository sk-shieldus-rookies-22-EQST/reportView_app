package com.example.bookies_001.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.NumberPicker
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

            val viewSearch = ViewSearch(viewAPI)
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

    private fun showCustomDatePicker(
        isStartDate: Boolean, // true면 시작 날짜 선택, false면 종료 날짜 선택
        currentDate: LocalDateTime?, // 현재 선택된 날짜 (초기값)
        onDateSelected: (LocalDateTime) -> Unit // 선택된 날짜를 반환하는 콜백 함수
    ) {
        val calendar = Calendar.getInstance()

        // 현재 날짜를 가져옴
        val currentYear = currentDate?.year ?: calendar.get(Calendar.YEAR)
        val currentMonth = currentDate?.monthValue ?: (calendar.get(Calendar.MONTH) + 1)
        val currentDay = currentDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

        // 다이얼로그 레이아웃 설정
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_data_picker, null)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val dayPicker = dialogView.findViewById<NumberPicker>(R.id.dayPicker)

        // 년도 설정
        yearPicker.minValue = 1950 // 최소 연도 설정
        yearPicker.maxValue = calendar.get(Calendar.YEAR) // 현재 연도까지 선택 가능
        yearPicker.value = currentYear

        // 월 설정
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = currentMonth

        // 일 설정
        updateDayPicker(yearPicker.value, monthPicker.value, dayPicker, currentDay)

        // 년도나 월이 변경될 때마다 일 값 업데이트
        yearPicker.setOnValueChangedListener { _, _, newYear ->
            updateDayPicker(newYear, monthPicker.value, dayPicker, dayPicker.value)
        }

        monthPicker.setOnValueChangedListener { _, _, newMonth ->
            updateDayPicker(yearPicker.value, newMonth, dayPicker, dayPicker.value)
        }

        // 다이얼로그 생성 및 버튼 추가
        AlertDialog.Builder(requireContext())
            .setTitle(if (isStartDate) "시작 날짜 선택" else "종료 날짜 선택")
            .setView(dialogView)
            .setPositiveButton("확인") { _, _ ->
                val selectedDate = LocalDateTime.of(
                    yearPicker.value,
                    monthPicker.value,
                    dayPicker.value,
                    if (isStartDate) 0 else 23, // 시작 날짜는 00:00:00, 종료 날짜는 23:59:59
                    if (isStartDate) 0 else 59
                )
                onDateSelected(selectedDate)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 년도와 월에 따라 일(NumberPicker)의 최대값을 동적으로 변경하는 함수
    private fun updateDayPicker(year: Int, month: Int, dayPicker: NumberPicker, currentDay: Int) {
        val maxDays = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28 // 윤년 계산
            else -> 30
        }

        dayPicker.minValue = 1
        dayPicker.maxValue = maxDays
        dayPicker.value = if (currentDay > maxDays) maxDays else currentDay
    }

    private fun showStartDatePicker() {
        showCustomDatePicker(
            isStartDate = true,
            currentDate = sFilterDate
        ) { selectedDate ->
            sFilterDate = selectedDate
            searchSdate.text = formatter.format(Calendar.getInstance().apply {
                set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
            }.time)
            Log.d("ListFragment", "시작 날짜 선택됨: $sFilterDate")

            // 종료 날짜 선택 다이얼로그 호출
            showEndDatePicker()
        }
    }


    private fun showEndDatePicker() {
        showCustomDatePicker(
            isStartDate = false,
            currentDate = eFilterDate
        ) { selectedDate ->
            eFilterDate = selectedDate
            searchEdate.text = formatter.format(Calendar.getInstance().apply {
                set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
            }.time)
            Log.d("ListFragment", "끝 날짜 선택됨: $eFilterDate")
        }
    }

}
