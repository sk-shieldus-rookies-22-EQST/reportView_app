package com.example.reportview_003.ui.board.action

import android.content.Context
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardQnAResponse
import com.example.reportview_003.repository.BoardRepository

class GetEachBoard(
    private val context: Context,
    private val boardAPI: BoardAPI
) {
    fun getBoardDetails(boardId: Int, callback: (BoardQnAResponse?) -> Unit) {
        val boardRepository = BoardRepository(boardAPI)

        boardRepository.showQna(boardId) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
                callback(null)
            }
        }
    }
}
