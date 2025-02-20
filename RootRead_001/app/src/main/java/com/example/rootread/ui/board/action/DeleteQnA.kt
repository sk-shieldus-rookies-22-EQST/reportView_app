package com.example.rootread.ui.board.action

import android.content.Context
import com.example.rootread.api.BoardAPI
import com.example.rootread.model.board.BoardDeleteRequest
import com.example.rootread.model.board.BoardDeleteResponse
import com.example.rootread.repository.BoardRepository

class DeleteQnA(
    private val context: Context,
    private val boardAPI: BoardAPI
) {
    fun deleteQnA(
        boardDeleteRequest: BoardDeleteRequest,
        callback: (BoardDeleteResponse) -> Unit
    ) {
        val boardRepository = BoardRepository(boardAPI)

        boardRepository.deleteQnA(boardDeleteRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }

    }

}