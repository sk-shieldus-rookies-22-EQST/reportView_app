package com.example.reportview_003.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reportview_003.ActiveMain
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardCommentRequest
import com.example.reportview_003.model.board.BoardDeleteRequest
import com.example.reportview_003.model.board.BoardQnAResponse
import com.example.reportview_003.repository.BoardRepository
import com.example.reportview_003.ui.board.action.CommentAdapter
import com.example.reportview_003.ui.board.action.DeleteQnA
import com.example.reportview_003.ui.board.action.GetEachBoard
import com.example.reportview_003.utils.SessionManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class EachBoardFragment : Fragment() {

    private lateinit var qnaTitle: TextView // 제목
    private lateinit var qnaId: TextView // 게시글 번호
    private lateinit var qnaFileDownload: ImageView // 파일 다운로드 버튼
    private lateinit var qnaUser: TextView // 작성자
    private lateinit var qnaDate: TextView // 작성일
    private lateinit var qnaInputText: TextView // 게시글 내용
    private lateinit var qnaCommentList: ListView // 댓글 내용
    private lateinit var qnaCommentInput: TextView // 댓글 입력
    private lateinit var qnaCommentButton: Button // 댓글 등록
    private lateinit var deleteButton: Button // 삭제 버튼
    private lateinit var modifyButton: Button // 수정 버튼
    private lateinit var listButton: Button // 목록 버튼

    // LocalDateTime 입력 포맷: "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    // 출력 포맷: "yyyy-MM-dd"
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
        qnaCommentList = view.findViewById(R.id.qna_comment)
        deleteButton = view.findViewById(R.id.qna_delete_button)
        modifyButton = view.findViewById(R.id.qna_edit_button)
        listButton = view.findViewById(R.id.qna_list_button)
        qnaFileDownload = view.findViewById(R.id.qna_file_download)
        qnaCommentInput = view.findViewById(R.id.qna_comment_input)
        qnaCommentButton = view.findViewById(R.id.qna_comment_button)

        val boardId = arguments?.getInt("qna_id",-1) ?: -1
        if (boardId != -1) {
            loadBoardDetails(boardAPI, boardId)
        } else {
            Log.e("EachBoardFragment", "Invalid board ID")
        }

        qnaFileDownload.setOnClickListener{
            // 파일 다운로드 기능 추가
        }

        qnaCommentButton.setOnClickListener {
            val boardCommentRequest = BoardCommentRequest(
                qna_id = boardId,
                writer = SessionManager.getUserID(requireContext()).toString(),
                content = qnaCommentInput.text.toString()
            )
            // 댓글 등록 기능 추가
            BoardRepository(boardAPI).writeComment(boardCommentRequest) { response, error ->
                if (response != null) {
                    if (response.status) {
                        loadBoardDetails(boardAPI, boardId)
                    } else {
                        Toast.makeText(requireContext(), "댓글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "댓글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        deleteButton.setOnClickListener {
            // 로그인 상태에 따라 페이지 이동
            if (!SessionManager.isLoggedIn(requireContext())) {
                Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.listFragment) // 루트 페이지로 이동

                (activity as? ActiveMain)?.apply {
                    navigationView.setCheckedItem(R.id.listFragment)
                }
                return@setOnClickListener
            } else if (qnaUser.text != SessionManager.getUserID(requireContext())) {
                Toast.makeText(requireContext(), "작성자만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val boardDeleteData = BoardDeleteRequest(
                board_id = boardId,
                writer = SessionManager.getUserID(requireContext()).toString()
            )
            val deleteQnA = DeleteQnA(requireContext(), boardAPI)
            deleteQnA.deleteQnA(boardDeleteData) { response ->
                if (response != null) {
                    navController.popBackStack()
                } else {
                    Log.e("EachBoardFragment", "Failed to delete board.")
                }
            }
        }

        modifyButton.setOnClickListener {
            // 로그인 상태에 따라 페이지 이동
            if (!SessionManager.isLoggedIn(requireContext())) {
                Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.listFragment) // 루트 페이지로 이동

                (activity as? ActiveMain)?.apply {
                    navigationView.setCheckedItem(R.id.listFragment)
                }
                return@setOnClickListener
            } else if (qnaUser.text != SessionManager.getUserID(requireContext())) {
                Toast.makeText(requireContext(), "작성자만 수정할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putInt("qna_id", boardId)
                putString("title", qnaTitle.text.toString())
                putString("content", qnaInputText.text.toString())
            }
            navController.navigate(R.id.action_eachBoardFragment_to_reWriterFragment, bundle)
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
                // comment는 리스트 형태로 서버에서 응답 받으며 복수의 comment가 등록 될 수 있음
                val adapter = CommentAdapter(requireContext(),response.comment)
                qnaCommentList.adapter = adapter
            } else {
                Log.e("EachBoardFragment", "Failed to load board details.")
            }
        }
    }

    // 서버로부터 받은 응답값으로 UI를 업데이트하는 함수
    private fun updateUI(boardQnAResponse: BoardQnAResponse) {
        qnaTitle.text = boardQnAResponse.title
        qnaId.text = boardQnAResponse.board_id.toString()
        qnaUser.text = boardQnAResponse.writer
        qnaInputText.text = boardQnAResponse.content
        val createdAt = boardQnAResponse.created_at
        qnaDate.text = try {
            val parsedDate = LocalDateTime.parse(createdAt, inputFormatter)
            parsedDate.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            Log.e("EachBoardFragment", "Date parsing error: ${e.message}")
            "날짜 없음"
        }
    }
}
