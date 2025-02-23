package f0.c.rootread.ui.board.action

import android.content.Context
import f0.c.rootread.api.BoardAPI
import f0.c.rootread.model.board.BoardResponse
import f0.c.rootread.repository.BoardRepository

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