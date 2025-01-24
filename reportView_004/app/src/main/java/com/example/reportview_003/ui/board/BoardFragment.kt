package com.example.reportview_003.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.ui.board.action.GetBoard
import com.example.reportview_003.ui.view.action.BuildBooklist

class BoardFragment : Fragment(), View.OnClickListener {

    private lateinit var boardListView: ListView

    private var boardData: MutableList<MutableMap<String, Any>> = mutableListOf()

    private fun updateUI(data: MutableList<MutableMap<String, Any>>) {
        if (isAdded) { // Fragment가 Activity에 연결되어 있는지 확인
            val context = requireContext() // 안전하게 context 호출
            val navController = findNavController()
            val adapter = BuildBooklist(context, data, navController)
            boardListView.adapter = adapter
        } else {
            Log.e("ListFragment", "Fragment is not attached to a context.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_main, container, false)

        boardListView = view.findViewById(R.id.board_list_view)

        val app = requireActivity().application as App
        val boardAPI = app.retrofit.create(BoardAPI::class.java)

        val getBoard = GetBoard(requireContext(), boardAPI)
        getBoard.loadBoardList { data ->
            if (isAdded) {
                boardData = data
                requireActivity().runOnUiThread {
                    boardListView.adapter = BoardAdapter(requireContext(), boardData)
                }
            } else {
                Log.e("ListFragment", "Fragment is not attached to a context while loading data.")
            }
        }

        return view
    }

    override fun onClick(v: View?) {
        if (isAdded) {
            updateUI(boardData)
        } else {
            Log.e("ListFragment", "Fragment is not attached to a context while handling click.")
        }
    }
}
