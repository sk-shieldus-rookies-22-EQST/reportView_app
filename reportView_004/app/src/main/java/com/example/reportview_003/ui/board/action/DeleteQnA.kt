package com.example.reportview_003.ui.board.action

import android.content.Context
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardDeleteRequest
import com.example.reportview_003.model.board.BoardDeleteResponse
import com.example.reportview_003.repository.BoardRepository

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