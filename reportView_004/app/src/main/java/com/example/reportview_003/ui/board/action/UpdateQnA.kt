package com.example.reportview_003.ui.board.action

import android.content.Context
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardCommentRequest
import com.example.reportview_003.model.board.BoardDeleteRequest
import com.example.reportview_003.model.board.BoardDeleteResponse
import com.example.reportview_003.model.board.BoardModifyRequest
import com.example.reportview_003.model.board.BoardModifyResponse
import com.example.reportview_003.repository.BoardRepository

class UpdateQnA(
    private val context: Context,
    private val boardAPI: BoardAPI
) {

    fun updateComment(
        boardModifyRequest: BoardModifyRequest,
        callback: (BoardModifyResponse) -> Unit
    ) {
        val boardRepository = BoardRepository(boardAPI)

        boardRepository.modifyQnA(boardModifyRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}