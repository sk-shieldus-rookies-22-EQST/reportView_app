package com.example.reportview_003.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardQnAResponse
import com.example.reportview_003.ui.board.action.GetEachBoard

class EachBoardFragment : Fragment() {

    private lateinit var qnaTitle: TextView
    private lateinit var qnaId: TextView
    private lateinit var qnaUser: TextView
    private lateinit var qnaDate: TextView
    private lateinit var qnaInputText: TextView
    private lateinit var qnaCommentText: TextView
    private lateinit var deleteButton: Button
    private lateinit var editButton: Button
    private lateinit var listButton: Button

    private var bundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.qna_detail_main, container, false)

        val app = requireActivity().application as App
        val boardAPI = app.retrofit.create(BoardAPI::class.java)
        val navController = findNavController()

        qnaTitle = view.findViewById(R.id.qna_title)
        qnaId = view.findViewById(R.id.qna_id)
        qnaUser = view.findViewById(R.id.qna_user)
        qnaDate = view.findViewById(R.id.qna_date)
        qnaInputText = view.findViewById(R.id.qna_input_text)
        qnaCommentText = view.findViewById(R.id.qna_comment)
        deleteButton = view.findViewById(R.id.qna_delete_button)
        editButton = view.findViewById(R.id.qna_edit_button)
        listButton = view.findViewById(R.id.qna_list_button)

        val boardId = arguments?.getInt("board_id",-1) ?: -1
        if (boardId != -1) {
            loadBoardDetails(boardAPI, boardId)
        } else {
            Log.e("EachBoardFragment", "Invalid board ID")
        }

        deleteButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        editButton.setOnClickListener {
            bundle.apply { "board_id" to boardId }
            navController.navigate(R.id.action_eachBoardFragment_to_qnaWriterFragment, bundle)
        }

        listButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    private fun loadBoardDetails(boardAPI: BoardAPI, boardId: Int) {
        val getEachBoard = GetEachBoard(requireContext(), boardAPI)
        getEachBoard.getBoardDetails(boardId) { response ->
            if (response != null) {
                updateUI(response)
            } else {
                Log.e("EachBoardFragment", "Failed to load board details.")
            }
        }
    }

    // 서버로부터 받은 응답값으로 UI를 업데이트하는 함수
    private fun updateUI(boardQnAResponse: BoardQnAResponse) {
        qnaTitle.text = boardQnAResponse.title
        qnaId.text = "ID: ${boardQnAResponse.board_id}"
        qnaUser.text = "작성자: ${boardQnAResponse.user_id}"
        qnaDate.text = "날짜: ${boardQnAResponse.date}"
        qnaInputText.text = boardQnAResponse.content
        qnaCommentText.text = boardQnAResponse.comment
    }
}
