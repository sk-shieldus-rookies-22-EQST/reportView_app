package f0.c.rootread.model.board


data class BoardCommentRequest(
    val qna_id: Long,
    val writer: String,
    val content: String
)
