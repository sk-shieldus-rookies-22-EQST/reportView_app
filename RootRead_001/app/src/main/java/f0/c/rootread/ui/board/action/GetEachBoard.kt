package f0.c.rootread.ui.board.action

import android.content.Context
import f0.c.rootread.api.BoardAPI
import f0.c.rootread.model.board.BoardQnAResponse
import f0.c.rootread.repository.BoardRepository

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
