package f0.c.rootread.ui.board.action

import android.content.Context
import f0.c.rootread.api.BoardAPI
import f0.c.rootread.model.board.BoardDeleteRequest
import f0.c.rootread.model.board.BoardDeleteResponse
import f0.c.rootread.repository.BoardRepository

class DeleteQnA(
    private val context: Context,
    private val boardAPI: BoardAPI
) {
    fun deleteQnA(
        boardDeleteRequest: BoardDeleteRequest,
        callback: (BoardDeleteResponse) -> Unit
    ) {
        val boardRepository = BoardRepository(boardAPI)

        boardRepository.deleteQnA(boardDeleteRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }

    }

}