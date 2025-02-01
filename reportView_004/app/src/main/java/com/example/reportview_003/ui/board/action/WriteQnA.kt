package com.example.reportview_003.ui.board.action

import android.content.Context
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.BoardWriteRequest
import com.example.reportview_003.repository.BoardRepository
import com.example.reportview_003.utils.SessionManager

class WriteQnA(
    private val context: Context,
    private val boardAPI: BoardAPI
) {

    fun submitQnA(title: String, content: String, callback: (Boolean) -> Unit) {
        val boardRepository = BoardRepository(boardAPI)

        var userId = SessionManager.getUserID(context) ?: "not logined"

        val request = BoardWriteRequest(
            title = title,
            content = content,
            writer = userId
        )

        boardRepository.writeQnA(request) { response, error ->
            if (response != null) {
                callback(true)
            } else {
                error?.printStackTrace()
                callback(false)
            }
        }
    }
}