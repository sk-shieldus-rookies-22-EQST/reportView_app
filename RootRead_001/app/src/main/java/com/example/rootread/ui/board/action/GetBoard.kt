package com.example.rootread.ui.board.action

import android.content.Context
import com.example.rootread.api.BoardAPI
import com.example.rootread.model.board.BoardResponse
import com.example.rootread.repository.BoardRepository

class GetBoard(
    private val context: Context,
    private val boardAPI: BoardAPI
) {
    fun loadBoardList(callback: (BoardResponse) -> Unit) {
        val boardlist = BoardRepository(boardAPI)

        boardlist.render { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}