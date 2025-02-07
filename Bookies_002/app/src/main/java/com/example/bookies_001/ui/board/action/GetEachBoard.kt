package com.example.bookies_001.ui.board.action

import android.content.Context
import com.example.bookies_001.api.BoardAPI
import com.example.bookies_001.model.board.BoardQnAResponse
import com.example.bookies_001.repository.BoardRepository

class GetEachBoard(
    private val context: Context,
    private val boardAPI: BoardAPI
) {
    fun getBoardDetails(boardId: Long, callback: (BoardQnAResponse?) -> Unit) {
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
