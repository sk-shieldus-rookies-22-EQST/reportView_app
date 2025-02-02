package com.example.reportview_003.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardResponse
import com.example.reportview_003.ui.board.action.GetBoard

class BoardFragment : Fragment() {

    private lateinit var boardListView: ListView
    private lateinit var boardWriteButton: ImageView
    private lateinit var boardData: BoardResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_main, container, false)

        boardListView = view.findViewById(R.id.board_list_view)
        boardWriteButton = view.findViewById(R.id.board_write_button)

        val app = requireActivity().application as App
        val boardAPI = app.retrofit.create(BoardAPI::class.java)

        loadBoardData(boardAPI)

        boardWriteButton.setOnClickListener {
            findNavController().navigate(R.id.action_boardFragment_to_qnaWriterFragment)
        }

        return view
    }

    private fun loadBoardData(boardAPI: BoardAPI) {
        val getBoard = GetBoard(requireContext(), boardAPI)
        getBoard.loadBoardList { data ->
            if (isAdded) {
                boardData = data
                requireActivity().runOnUiThread {
                    updateUI(boardData)
                }
            } else {
                Log.e("BoardFragment", "Fragment is not attached to a context while loading data.")
            }
        }
    }

    private fun updateUI(data: BoardResponse) {
        if (isAdded) {
            val adapter = BoardAdapter(requireContext(), data, findNavController())
            boardListView.adapter = adapter
            setItemClickListener()
        } else {
            Log.e("BoardFragment", "Fragment is not attached to a context.")
        }
    }

    private fun setItemClickListener() {
        boardListView.setOnItemClickListener { _, _, position, _ ->
            val selectedBoard = boardData.qnaListDto[position]
            val boardId = (selectedBoard["qna_id"] as? Number)?.toLong() ?: -1.0

            if (boardId != -1) {
                val bundle = Bundle().apply {
                    putLong("qna_id", boardId.toLong())
                }
                findNavController().navigate(R.id.action_boardFragment_to_eachBoardFragment, bundle)
            } else {
                Log.e("BoardFragment", "Invalid board_id: $selectedBoard")
            }
        }
    }
}
