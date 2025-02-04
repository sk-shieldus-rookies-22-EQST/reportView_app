package com.example.bookies_001.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.BoardAPI
import com.example.bookies_001.model.board.BoardResponse
import com.example.bookies_001.ui.board.action.GetBoard
import com.example.bookies_001.utils.SessionManager

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

            // "secret" 키의 값이 true인 경우 클릭하지 않도록 처리 (true 또는 "true" 등 상황에 맞게 변환)
            if (selectedBoard["secret"] == true && selectedBoard["user_id"] != SessionManager.getUserID(requireContext()).toString()) {
                // 필요 시 사용자에게 알림 메시지를 표시할 수 있습니다.
                // Toast.makeText(requireContext(), "비공개 게시글은 열람할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

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
