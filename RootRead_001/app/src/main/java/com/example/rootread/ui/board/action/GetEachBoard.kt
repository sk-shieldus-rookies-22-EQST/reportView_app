package com.example.rootread.ui.board.action

import android.content.Context
import com.example.rootread.api.BoardAPI
import com.example.rootread.model.board.BoardQnAResponse
import com.example.rootread.repository.BoardRepository

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
