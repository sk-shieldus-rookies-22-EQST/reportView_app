package com.example.bookies_001.ui.board.action

import android.content.Context
import com.example.bookies_001.api.BoardAPI
import com.example.bookies_001.model.board.BoardDeleteRequest
import com.example.bookies_001.model.board.BoardDeleteResponse
import com.example.bookies_001.repository.BoardRepository

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