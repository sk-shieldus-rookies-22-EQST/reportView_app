package com.example.reportview_003.ui.board.action

import android.content.Context
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.repository.BoardRepository

class GetBoard(
    private val context: Context,
    private val boardAPI: BoardAPI
) {
    fun loadBoardList(callback: (MutableList<MutableMap<String, Any>>) -> Unit) {
        val boardlist = BoardRepository(boardAPI)

        boardlist.render { response, error ->
            if (response != null) {
                callback(response.qna)
            } else {
                error?.printStackTrace()
            }
        }
    }
}