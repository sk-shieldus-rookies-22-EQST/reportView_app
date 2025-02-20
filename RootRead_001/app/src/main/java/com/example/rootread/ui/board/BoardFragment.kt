package com.example.rootread.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.BoardAPI
import com.example.rootread.model.board.BoardResponse
import com.example.rootread.ui.board.action.GetBoard
import com.example.rootread.utils.SessionManager

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

            // 현재 사용자 정보 가져오기
            val userId = SessionManager.getUserID(requireContext()).toString()
            val userLevel = SessionManager.getUserLevel(requireContext())

            // "secret" 키의 값이 true인 경우 확인
            val isSecret = selectedBoard["secret"] == true
            val boardOwnerId = selectedBoard["user_id"]?.toString()

            // 비공개 게시글이면서 현재 사용자가 글 작성자가 아니고, userLevel이 123이 아니면 클릭 막기
            if (isSecret && boardOwnerId != userId && userLevel != 123) {
//                Toast.makeText(requireContext(), "비공개 게시글은 열람할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

            // qna_id를 Long으로 변환
            val boardId = (selectedBoard["qna_id"] as? Number)?.toLong() ?: -1L

            if (boardId != -1L) {
                val bundle = Bundle().apply {
                    putLong("qna_id", boardId)
                }
                findNavController().navigate(R.id.action_boardFragment_to_eachBoardFragment, bundle)
            } else {
                Log.e("BoardFragment", "Invalid board_id: $selectedBoard")
            }
        }
    }

}
