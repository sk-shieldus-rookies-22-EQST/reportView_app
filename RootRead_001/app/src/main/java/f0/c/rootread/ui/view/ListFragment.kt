package f0.c.rootread.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import f0.c.rootread.App
import f0.c.rootread.R
import f0.c.rootread.api.ViewAPI
import f0.c.rootread.model.view.ViewbooklistResponse
import f0.c.rootread.model.view.ViewbooksearchRequest
import f0.c.rootread.ui.view.action.BuildBooklist
import f0.c.rootread.ui.view.action.GetList
import java.text.SimpleDateFormat
import java.util.Calendar

/*
* 필터링 검색 기능을 가지고 있어야 함
* data에 모든 값을 저장하고 필터링 된 값을 리스트 뷰에 뿌려주는 형식
*/

class ListFragment : Fragment() {

    private lateinit var itemList: RecyclerView
    private lateinit var searchReport: EditText
    private lateinit var searchBtn: ImageView
    private lateinit var filterBtn: ImageView
    private lateinit var searchSdate: TextView
    private lateinit var searchEdate: TextView


    private lateinit var resData: ViewbooklistResponse

    // UI에 표시할 형식 ("yyyy-MM-dd")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    private fun updateUI(data: ViewbooklistResponse) {
        if (isAdded) { // Fragment가 Activity에 연결되어 있는지 확인
            val layoutManager = GridLayoutManager(requireContext(), 3)
            itemList.layoutManager = layoutManager
            val adapter = BuildBooklist(requireContext(), data.book_list, findNavController())
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

            if ((searchSdate.text.toString() == null) xor (searchEdate.text.toString() == null)) {
                Toast.makeText(requireContext(), "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val viewSearchRequest = ViewbooksearchRequest (
                keyword = searchReport.getTextOrNull(),
                sdate = searchSdate.getTextOrNull(),
                edate = searchEdate.getTextOrNull()
            )

            Log.d("ListFragment", "검색 요청 JSON: $viewSearchRequest")

            val viewSearch = f0.c.rootread.ui.view.action.ViewSearch(viewAPI)
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish() // 앱 종료
            }
        })

        return view
    }

    private fun showCustomDatePicker(
        isStartDate: Boolean,
        onDateSelected: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_data_picker, null)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val dayPicker = dialogView.findViewById<NumberPicker>(R.id.dayPicker)

        yearPicker.minValue = 1980
        yearPicker.maxValue = calendar.get(Calendar.YEAR)
        yearPicker.value = calendar.get(Calendar.YEAR)

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = calendar.get(Calendar.MONTH) + 1

        updateDayPicker(yearPicker.value, monthPicker.value, dayPicker, calendar.get(Calendar.DAY_OF_MONTH))

        yearPicker.setOnValueChangedListener { _, _, newYear ->
            updateDayPicker(newYear, monthPicker.value, dayPicker, dayPicker.value)
        }

        monthPicker.setOnValueChangedListener { _, _, newMonth ->
            updateDayPicker(yearPicker.value, newMonth, dayPicker, dayPicker.value)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (isStartDate) "시작 날짜 선택" else "종료 날짜 선택")
            .setView(dialogView)
            .setPositiveButton("확인") { _, _ ->
                val selectedDate = String.format("%04d-%02d-%02d", yearPicker.value, monthPicker.value, dayPicker.value)
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
        showCustomDatePicker(isStartDate = true) { selectedDate ->
            searchSdate.text = selectedDate
            showEndDatePicker()
        }
    }

    private fun showEndDatePicker() {
        showCustomDatePicker(isStartDate = false) { selectedDate ->
            searchEdate.text = selectedDate
        }
    }

    private fun EditText.getTextOrNull(): String? {
        val text = this.text.toString().trim() // 앞뒤 공백 제거
        return if (text.isEmpty()) null else text
    }

    private fun TextView.getTextOrNull(): String? {
        val text = this.text.toString().trim() // 앞뒤 공백 제거
        return if (text.isEmpty()) null else text
    }

}
