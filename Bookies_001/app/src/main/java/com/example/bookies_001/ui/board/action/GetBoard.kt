package com.example.bookies_001.ui.board.action

import android.content.Context
import com.example.bookies_001.api.BoardAPI
import com.example.bookies_001.model.board.BoardResponse
import com.example.bookies_001.repository.BoardRepository

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