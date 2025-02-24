package f0.c.rootread.ui.board.action

import android.content.Context
import f0.c.rootread.api.BoardAPI
import f0.c.rootread.model.board.BoardModifyRequest
import f0.c.rootread.model.board.BoardModifyResponse
import f0.c.rootread.repository.BoardRepository

class UpdateQnA(
    private val context: Context,
    private val boardAPI: BoardAPI
) {

    fun updateComment(
        boardModifyRequest: BoardModifyRequest,
        callback: (BoardModifyResponse) -> Unit
    ) {
        val boardRepository = BoardRepository(boardAPI)

        boardRepository.modifyQnA(boardModifyRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}