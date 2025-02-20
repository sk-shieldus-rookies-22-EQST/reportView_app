package com.example.rootread.ui.board.action

import android.content.Context
import com.example.rootread.api.BoardAPI
import com.example.rootread.model.board.BoardModifyRequest
import com.example.rootread.model.board.BoardModifyResponse
import com.example.rootread.repository.BoardRepository

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