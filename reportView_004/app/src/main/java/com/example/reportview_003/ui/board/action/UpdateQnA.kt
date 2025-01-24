package com.example.reportview_003.ui.board.action

import android.content.Context
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardCommentRequest
import com.example.reportview_003.repository.BoardRepository

class UpdateQnA(
    private val context: Context,
    private val boardAPI: BoardAPI
) {

    fun updateComment(
        boardCommentRequest: BoardCommentRequest,
        callback: (Boolean) -> Unit
    ) {
        val boardRepository = BoardRepository(boardAPI)

        boardRepository.writeComment(boardCommentRequest) { response, error ->
            if (response != null && response.status == "success") {
                callback(true)
            } else {
                error?.printStackTrace()
                callback(false)
            }
        }
    }
}