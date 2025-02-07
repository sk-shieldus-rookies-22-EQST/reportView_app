package com.example.bookies_001.ui.board.action

import android.content.Context
import com.example.bookies_001.api.BoardAPI
import com.example.bookies_001.model.board.BoardModifyRequest
import com.example.bookies_001.model.board.BoardModifyResponse
import com.example.bookies_001.repository.BoardRepository

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