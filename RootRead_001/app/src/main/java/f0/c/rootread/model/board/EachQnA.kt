package f0.c.rootread.model.board

data class EachQnA(
    val qna_id:Long ,
    val title: String ,
    val user_id: String,
    val secret: Boolean,
    val created_at: String
)
